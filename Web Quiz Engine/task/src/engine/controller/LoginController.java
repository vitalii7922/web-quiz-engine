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
        System.out.println("redirect opening page");
        return "redirect:/opening-page";
    }

    @GetMapping("/open")
    public String open() {
        return "page";
    }

    @GetMapping("/opening-page")
    public String something() {
        return "hello";
    }
}
