package engine.model;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(quizzes, user.quizzes) &&
                Objects.equals(completedQuizzes, user.completedQuizzes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, quizzes, completedQuizzes);
    }
}