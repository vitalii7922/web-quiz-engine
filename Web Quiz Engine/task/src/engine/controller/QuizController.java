package engine.controller;

import engine.model.Answer;
import engine.model.Quiz;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;

@RestController
public class QuizController {

    @GetMapping(path = "/api/quiz")
    public Quiz getQuiz() {
        return Quiz.builder()
                .title("The Java logo")
                .text("What is depicted on the Java logo?")
                .options(Arrays.asList("Robot", "Tea leaf", "Cup of coffee", "Bug"))
                .build();
    }

    @PostMapping(path = "/api/quiz")
    public Answer solveQuiz(@RequestParam int answer) {
        return answer == 2 ? new Answer(true, "Congratulations, you're right!")
                : new Answer(false, "Wrong answer! Please, try again.");
    }
}
