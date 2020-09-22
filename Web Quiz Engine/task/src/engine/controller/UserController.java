package engine.controller;

import engine.dto.UserDto;
import engine.service.UserService;
import engine.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userDetailsService;

    public UserController(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> authorizeUser(@Valid @RequestBody final UserDto userDto) {
        Optional<User> userOptional = Optional.ofNullable(userDetailsService.registerUser(userDto));
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Account with email %s already exists",
                userDto.getEmail()));
    }
}
