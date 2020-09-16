package engine.service;

import engine.controller.QuizController;
import engine.dto.QuizDto;
import engine.mapper.QuizMapper;
import engine.model.*;
import engine.repository.CompletedQuizRepository;
import engine.repository.QuizRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    private final CompletedQuizRepository completedQuizRepository;

    private final UserService userService;

    private final QuizMapper quizMapper;

    private static final int NUMBER_OF_ELEMENTS = 10;

    public QuizService(QuizRepository quizRepository, CompletedQuizRepository completedQuizRepository,
                       UserService userService, QuizMapper quizMapper) {
        this.quizRepository = quizRepository;
        this.completedQuizRepository = completedQuizRepository;
        this.userService = userService;
        this.quizMapper = quizMapper;
    }

    public Quiz getQuizById(final long id) {
        return quizRepository.findById(id);
    }

    public QuizDto addQuiz(QuizDto quizDto) {
        Quiz quiz = quizMapper.toQuiz(quizDto);
        quiz.setUser(userService.getCurrentUser());
        quizRepository.save(quiz);
        return quizDto;
    }

    public Page<Quiz> getAllQuizzesByPageNumber(int pageNumber) {
        List<Quiz> quizList = quizRepository.findAll();
        if (!CollectionUtils.isEmpty(quizList)) {
            PageRequest page = PageRequest.of(--pageNumber, NUMBER_OF_ELEMENTS, Sort.by("id"));
            return new PageImpl<>(quizRepository.findAll(page).getContent(), page, quizList.size());
        }
        return Page.empty();
    }

    public boolean answerIsCorrect(final Answer answer, final Quiz quiz) {
        if (answer.getAnswer().equals(quiz.getAnswer())) {
            CompletedQuiz completedQuiz = CompletedQuiz.builder()
                    .user(userService.getCurrentUser())
                    .id(quiz.getId())
                    .completedAt(LocalDateTime.now())
                    .build();
            completedQuizRepository.save(completedQuiz);
            return true;
        }
        return false;
    }

    public QuizDto updateQuizById(QuizDto quizDto, final long id) {
        Optional<Quiz> optionalQuiz = Optional.ofNullable(getQuizById(id));
        if (optionalQuiz.isPresent()) {
            Quiz quizResult = optionalQuiz.get();
            if (quizResult.getUser().getId() == userService.getCurrentUser().getId()) {
                quizResult.setAnswer(quizDto.getAnswer());
                quizResult.setOptions(quizDto.getOptions());
                quizRepository.save(quizResult);
                return quizMapper.toQuizDto(quizResult);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        String.format("You haven't created quiz with id %d", id));
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(QuizController.QUIZ_NOT_FOUND, id));
    }

    public HttpStatus deleteQuizById(final long id) {
        Optional<Quiz> optionalQuiz = Optional.ofNullable(getQuizById(id));
        if (optionalQuiz.isPresent()) {
            User user = optionalQuiz.get().getUser();
            if (user != null
                    && user.getId() == userService.getCurrentUser().getId()) {
                quizRepository.delete(optionalQuiz.get());
                return HttpStatus.NO_CONTENT;
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        String.format("You haven't created quiz with id %d", id));
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format(QuizController.QUIZ_NOT_FOUND, id));
    }

    public Page<CompletedQuiz> getCompletedQuizzes(int pageNumber) {
        long userId = userService.getCurrentUser().getId();
        List<CompletedQuiz> completedQuizList = completedQuizRepository.findAllByUserId(userId);
        if (!CollectionUtils.isEmpty(completedQuizList)) {
            PageRequest page = PageRequest.of(--pageNumber, NUMBER_OF_ELEMENTS, Sort.by("completedAt").descending());
            List<CompletedQuiz> page1 = completedQuizRepository.findAllByUserId(userId, page).getContent();
            return new PageImpl<>(page1, page,
                    completedQuizList.size());
        }
        return Page.empty();
    }
}
