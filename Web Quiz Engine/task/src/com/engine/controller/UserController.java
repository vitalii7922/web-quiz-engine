package com.engine.controller;

import com.engine.dto.UserDto;
import com.engine.service.UserService;
import com.engine.model.User;
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

    /**Constructor for UserController
     *
     * @param userDetailsService has methods for obtaining user's information
     */
    public UserController(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * @param userDto user object
     * @return HTTP status OK if registered or BAD REQUEST with message if a user with the same email registered
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody final UserDto userDto) {
        Optional<User> userOptional = Optional.ofNullable(userDetailsService.registerUser(userDto));
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Account with email %s already exists",
                userDto.getEmail()));
    }
}
