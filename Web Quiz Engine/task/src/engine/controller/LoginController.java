package engine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @GetMapping("/")
    public String getIndex() {
        return "login";
    }

    @GetMapping("/signUp")
    public String signUp() {
        return "signup";
    }

    @PostMapping("/postLogin")
    public String successLogIn() {
        return "redirect:/opening-page";
    }

    @GetMapping("/openQuizList")
    public String openQuizList() {
        return "quizList";
    }

    @GetMapping("/openQuiz")
    public String openQuiz() {
        return "quiz";
    }

    @GetMapping("/opening-page")
    public String something() {
        return "addQuiz";
    }

    @GetMapping("/addedQuiz")
    public String accessDenied() {
        return "addedQuiz";
    }

    @GetMapping("/updateQuiz")
    public String updateQuiz() {
        return "updateQuiz";
    }

    @GetMapping("/updatedQuiz")
    public String updatedQuiz() {
        return "updatedQuiz";
    }

    @GetMapping("/openCompletedQuizzes")
    public String getCompletedQuizzes() {
        return "quizListCompleted";
    }
}
