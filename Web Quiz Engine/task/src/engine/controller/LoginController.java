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
    @PostMapping("/postLogin")
    public String successLogIn() {
        System.out.println("you are here");
        return "redirect:/opening-page";
    }

    @GetMapping("/opening-page")
    public String something() {
        return "hello";
    }
}
