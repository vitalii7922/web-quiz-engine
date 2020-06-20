package engine.controller;

import engine.model.Answer;
import engine.model.Quiz;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final Map<Long, Quiz> quizzes = new LinkedHashMap<>();
    private final Answer success = new Answer(true);
    private final Answer fail = new Answer(false);

    @GetMapping
    public Collection<Quiz> getQuiz() {
        return quizzes.values();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable long id) {
        return Optional.ofNullable(quizzes.get(id))
                .map(quiz -> ResponseEntity.ok().body(quiz))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Quiz addQuiz(@Valid @RequestBody Quiz quiz) {
        quiz.setId(quizzes.size() + 1L);
        if (quiz.getAnswer() == null) {
            quiz.setAnswer(new ArrayList<>());
        }
        quizzes.put(quiz.getId(), quiz);
        return quiz;
    }

    @PostMapping(path = "/{id}/solve")
    public ResponseEntity<Answer> solveQuiz(@RequestBody Quiz quizWithAnswer, @PathVariable long id) {
        return Optional.ofNullable(quizzes.get(id)).map(quiz -> quizWithAnswer.getAnswer().equals(quiz.getAnswer())
                ? ResponseEntity.ok().body(success)
                : ResponseEntity.ok().body(fail))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
