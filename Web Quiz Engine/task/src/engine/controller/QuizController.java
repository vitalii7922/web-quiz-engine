package engine.controller;

import engine.dto.QuizDto;
import engine.model.Answer;
import engine.model.CompletedQuiz;
import engine.model.Quiz;
import engine.model.Result;
import engine.service.QuizService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public static final String QUIZ_NOT_FOUND = "QUIZ DOESN'T EXIST WITH ID %d";

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<Page<Quiz>> getAllQuizzes(@RequestParam("page") final Integer pageNumber) {
        Page<Quiz> page = quizService.getAllQuizzesByPageNumber(pageNumber);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable("id") final long id) {
        return Optional.ofNullable(quizService.getQuizById(id))
                .map(quiz -> ResponseEntity.ok().body(quiz))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(QUIZ_NOT_FOUND, id)));
    }

    @GetMapping(path = "/completed")
    public ResponseEntity<Page<CompletedQuiz>> getCompletedQuizzes(@RequestParam(value = "page") final Integer pageNumber) {
        return new ResponseEntity<>(quizService.getCompletedQuizzes(pageNumber), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<QuizDto> addQuiz(@Valid @RequestBody final QuizDto quizDto) {
        return new ResponseEntity<>(quizService.addQuiz(quizDto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Quiz> deleteQuiz(@PathVariable("id") final long id) {
        return new ResponseEntity<>(quizService.deleteQuizById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDto> updateQuiz(@Valid @RequestBody final QuizDto quizDto, @PathVariable("id") final long id) {
        return new ResponseEntity<>(quizService.updateQuizById(quizDto, id), HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/solve")
    public ResponseEntity<Result> solveQuiz(@RequestBody final Answer answer, @PathVariable final long id) {
        return Optional.ofNullable(quizService.getQuizById(id)).map(quiz -> quizService.answerIsCorrect(answer, quiz)
                ? ResponseEntity.ok().body(Result.SUCCESS_RESULT)
                : ResponseEntity.ok().body(Result.FAILURE_RESULT))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(QUIZ_NOT_FOUND, id)));
    }
}