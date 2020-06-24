package engine.controller;
import engine.service.UserService;
import engine.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/register")
public class UserController {

    private final UserService userDetailsService;

    public UserController(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<User> authorizeUser(@Valid @RequestBody final User user) {
        Optional<User> userOptional = Optional.ofNullable(userDetailsService.registerUser(user));
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Account with email %s already exists",
                user.getEmail()));
    }
}
