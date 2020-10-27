package com.engine.service;

import com.engine.controller.QuizController;
import com.engine.mapper.QuizMapper;
import com.engine.model.CompletedQuiz;
import com.engine.model.Quiz;
import com.engine.model.User;
import com.engine.repository.CompletedQuizRepository;
import com.engine.repository.QuizRepository;
import com.engine.dto.Answer;
import com.engine.dto.CompletedQuizDto;
import com.engine.dto.QuizDto;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.server.ResponseStatusException;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    private final CompletedQuizRepository completedQuizRepository;

    private final UserService userService;

    private final QuizMapper quizMapper;

    private static final int NUMBER_OF_ELEMENTS = 5;

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

    public Page<QuizDto> getAllQuizzesByPageNumber(int pageNumber) {
        long quizNumber = quizRepository.countAll();
        if (quizNumber > 0) {
            PageRequest page = PageRequest.of(--pageNumber, NUMBER_OF_ELEMENTS, Sort.by("id"));
            List<QuizDto> quizDtoList = quizRepository.findAll(page).getContent()
                    .stream()
                    .map(quizMapper::toQuizDto).collect(Collectors.toList());
            quizDtoList.forEach(quizDto -> {
                if (quizDto.getUser().getId() == userService.getCurrentUser().getId()) {
                    quizDto.setModifiable(true);
                }
            });
            return new PageImpl<>(quizDtoList, page, quizNumber);
        }
        return Page.empty();
    }

    public boolean answerIsCorrect(final Answer answer, final Quiz quiz) {
        if (answer.getAnswers().equals(quiz.getAnswer())) {
            CompletedQuiz completedQuiz = CompletedQuiz.builder()
                    .user(userService.getCurrentUser())
                    .id(quiz.getId())
                    .title(quiz.getTitle())
                    .text(quiz.getText())
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

    public Page<CompletedQuizDto> getCompletedQuizzes(int pageNumber) {
        long userId = userService.getCurrentUser().getId();
        long completedQuizAmount = completedQuizRepository.countAllByUserId(userId);
        if (completedQuizAmount > 0) {
            PageRequest page = PageRequest.of(--pageNumber, NUMBER_OF_ELEMENTS, Sort.by("completedAt").descending());
            Page<CompletedQuiz> page1 = completedQuizRepository.findAllByUserId(userId, page);
            return new PageImpl<>(completedQuizRepository.findAllByUserId(userId, page).getContent()
                    .stream()
                    .map(quizMapper::toCompletedQuizDto)
                    .collect(Collectors.toList()), page, completedQuizAmount);
        }
        return Page.empty();
    }
}