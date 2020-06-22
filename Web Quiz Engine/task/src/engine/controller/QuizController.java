package engine.controller;

import engine.model.Answer;
import engine.model.Quiz;
import engine.model.Result;
import engine.service.QuizService;
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

    public static final String SERVICE_WARNING_MESSAGE = "QUIZ DOESN'T EXIST WITH ID %d";

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return new ResponseEntity<>(quizService.getAllQuizzes(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable("id") final long id) {
        return Optional.ofNullable(quizService.getQuizById(id))
                .map(quiz -> ResponseEntity.ok().body(quiz))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(SERVICE_WARNING_MESSAGE, id)));
    }

    @PostMapping
    public ResponseEntity<Quiz> addQuiz(@Valid @RequestBody final Quiz quiz) {
        return new ResponseEntity<>(quizService.addQuiz(quiz), HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/solve")
    public ResponseEntity<Result> solveQuiz(@RequestBody final Answer answer, @PathVariable final long id) {
        return Optional.ofNullable(quizService.getQuizById(id)).map(quiz -> quizService.answerIsCorrect(answer, quiz)
                ? ResponseEntity.ok().body(Result.SUCCESS_RESULT)
                : ResponseEntity.ok().body(Result.FAILURE_RESULT))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(SERVICE_WARNING_MESSAGE, id)));
    }
}