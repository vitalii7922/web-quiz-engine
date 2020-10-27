package com.engine.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @Email(message = "Incorrect format of email")
    @Pattern(regexp = ".+\\..{2,3}", message = "Incorrect format of email")
    private String email;
    @NotBlank
    @Size(min = 5, message = "Number of symbols has not to be less 5")
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Quiz> quizzes;
    @OneToMany(mappedBy = "user")
    private List<CompletedQuiz> completedQuizzes;
}