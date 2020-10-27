package com.engine.controller;

import com.engine.dto.CompletedQuizDto;
import com.engine.dto.QuizDto;
import com.engine.error.ApiError;
import com.engine.dto.Answer;
import com.engine.mapper.QuizMapper;
import com.engine.model.Quiz;
import com.engine.dto.Result;
import com.engine.service.QuizService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    private final QuizMapper quizMapper;

    public static final String QUIZ_NOT_FOUND = "QUIZ DOESN'T EXIST WITH ID %d";

    public QuizController(QuizService quizService, QuizMapper quizMapper) {
        this.quizService = quizService;
        this.quizMapper = quizMapper;
    }

    /**
     * @param pageNumber number of page in list of quizzes
     * @return JSON with list of quizzes according to number of page
     */
    @GetMapping
    public ResponseEntity<Page<QuizDto>> getAllQuizzes(@RequestParam("page") final Integer pageNumber) {
        Page<QuizDto> page = quizService.getAllQuizzesByPageNumber(pageNumber);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @param id quiz id
     * @return JSON with a quiz with HTTP status ok or error (quiz not found)
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable("id") final long id) {
        Quiz quiz = quizService.getQuizById(id);
        return Optional.ofNullable(quiz)
                .map(q -> ResponseEntity.ok().body(quizMapper.toQuizDto(q)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(QUIZ_NOT_FOUND, id)));
    }

    /**
     * @param pageNumber number of page in list of completed quizzes
     * @return JSON with list of completed quizzes by a user with HTTP status ok
     */
    @GetMapping(path = "/completed")
    public ResponseEntity<Page<CompletedQuizDto>> getCompletedQuizzes(@RequestParam(value = "page") final Integer pageNumber) {
        return new ResponseEntity<>(quizService.getCompletedQuizzes(pageNumber), HttpStatus.OK);
    }

    /**
     * @param quizDto quiz
     * @return JSON with quiz and HTTP status ok
     */
    @PostMapping
    public ResponseEntity<QuizDto> addQuiz(@Valid @RequestBody final QuizDto quizDto) {
        return new ResponseEntity<>(quizService.addQuiz(quizDto), HttpStatus.OK);
    }

    /**
     * @param id quiz id
     * @return http status no content
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Quiz> deleteQuiz(@PathVariable("id") final long id) {
        return new ResponseEntity<>(quizService.deleteQuizById(id));
    }

    /**
     * @param quizDto quiz
     * @param id quiz id
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizDto> updateQuiz(@Valid @RequestBody final QuizDto quizDto, @PathVariable("id") final long id) {
        return new ResponseEntity<>(quizService.updateQuizById(quizDto, id), HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/solve")
    public ResponseEntity<Result> solveQuiz(@RequestBody final Answer answer, @PathVariable final long id) {
        return Optional.ofNullable(quizService.getQuizById(id)).map(quiz -> quizService.answerIsCorrect(answer, quiz)
                ? ResponseEntity.ok().body(Result.SUCCESS_RESULT)
                : ResponseEntity.ok().body(Result.FAILURE_RESULT))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(QUIZ_NOT_FOUND, id)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}