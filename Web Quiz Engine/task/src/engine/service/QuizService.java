package engine.service;

import engine.controller.QuizController;
import engine.model.*;
import engine.repository.CompletedQuizRepository;
import engine.repository.QuizRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    private final CompletedQuizRepository completedQuizRepository;

    private final UserService userService;

    private static final int NUMBER_OF_ELEMENTS = 10;

    public QuizService(QuizRepository quizRepository, CompletedQuizRepository completedQuizRepository,
                       UserService userService) {
        this.quizRepository = quizRepository;
        this.completedQuizRepository = completedQuizRepository;
        this.userService = userService;
    }

    public Quiz getQuizById(final long id) {
        return quizRepository.findById(id);
    }

    public Quiz addQuiz(Quiz quiz) {
        quiz.setUser(userService.getCurrentUser());
        if (quiz.getAnswer() == null) {
            quiz.setAnswer(new ArrayList<>());
        }
        return quizRepository.save(quiz);
    }

    public Page<Quiz> getAllQuizzesByPageNumber(int pageNumber) {
        List<Quiz> quizList = quizRepository.findAll();
        if (!CollectionUtils.isEmpty(quizList)) {
            PageRequest page = PageRequest.of(pageNumber, NUMBER_OF_ELEMENTS, Sort.by("id"));
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
        List<CompletedQuiz> completedQuizList =
                completedQuizRepository.findAllByUserId(userId);
        if (!CollectionUtils.isEmpty(completedQuizList)) {
            PageRequest page = PageRequest.of(pageNumber, NUMBER_OF_ELEMENTS, Sort.by("completedAt").descending());
            return new PageImpl<>(completedQuizRepository.findAllByUserId(userId, page).getContent(), page,
                    completedQuizList.size());
        }
        return Page.empty();
    }
}
