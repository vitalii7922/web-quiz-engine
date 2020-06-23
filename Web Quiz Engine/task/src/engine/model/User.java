package engine.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @Email(message = "Incorrect format of email")
    private String email;
    @NotBlank
    @Size(min = 5, message = "Number of symbols has not to be less 5")
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Quiz> quizzes;
}
