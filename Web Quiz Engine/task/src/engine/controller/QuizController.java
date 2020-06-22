package engine.controller;
import engine.model.Answer;
import engine.model.Quiz;
import engine.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final Map<Long, Quiz> quizzes = new LinkedHashMap<>();

    private static final String SERVICE_WARNING_MESSAGE = "QUIZ DOESN'T EXIST WITH ID %d";

    @GetMapping
    public Collection<Quiz> getQuiz() {
        return quizzes.values();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable long id) {
        return Optional.ofNullable(quizzes.get(id))
                .map(quiz -> ResponseEntity.ok().body(quiz))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(SERVICE_WARNING_MESSAGE, id)));
    }

    @PostMapping
    public Quiz addQuiz(@Valid @RequestBody Quiz quiz) {
        quiz.setId(quizzes.size() + 1L);
        quizzes.put(quiz.getId(), quiz);
        return quiz;
    }

    @PostMapping(path = "/{id}/solve")
    public ResponseEntity<Result> solveQuiz(@RequestBody Answer answer, @PathVariable long id) {
        return Optional.ofNullable(quizzes.get(id)).map(quiz -> answer.getAnswer().equals(quiz.getAnswer())
                ? ResponseEntity.ok().body(Result.success())
                : ResponseEntity.ok().body(Result.fail()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(SERVICE_WARNING_MESSAGE, id)));
    }
}
