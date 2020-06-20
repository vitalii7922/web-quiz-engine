package engine.controller;
import engine.model.Answer;
import engine.model.Quiz;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class QuizController {

    private final Map<Long, Quiz> quizzes = new LinkedHashMap<>();

    @GetMapping(path = "/api/quizzes")
    public Collection<Quiz> getQuiz() {
        return quizzes.values();
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable long id) {
        return Optional.ofNullable(quizzes.get(id))
                .map(quiz -> ResponseEntity.ok().body(quiz))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/api/quizzes")
    public Quiz addQuiz(@RequestBody Quiz quiz) {
        quiz.setId(quizzes.size() + 1L);
        quiz.setAnswer(quiz.getAnswer());
        quizzes.put(quiz.getId(), quiz);
        return quiz;
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public ResponseEntity<Answer> solveQuiz(@RequestParam(required = false) int answer, @PathVariable long id) {
        return Optional.ofNullable(quizzes.get(id)).map(quiz -> answer == quiz.getAnswer() ?
                ResponseEntity.ok().body(new Answer(true, "Congratulations, you're right!"))
                : ResponseEntity.ok().body(new Answer(false, "Wrong answer! Please, try again.")))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
