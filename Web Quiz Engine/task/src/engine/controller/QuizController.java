package engine.controller;
import engine.model.Quiz;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

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
    public Map<Boolean, String> solveQuiz(@RequestParam int answer) {
        return answer == 2 ? Collections.singletonMap(true, "Congratulations, you're right!") :
                Collections.singletonMap(false, "Wrong answer! Please, try again.");
    }
}
