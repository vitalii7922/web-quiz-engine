package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CompletedQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @Column(name = "id")
    private long completedQuizId;

    @Column(name = "quiz_id")
    private long id;

    private LocalDateTime completedAt;

    @ManyToOne
    @JsonIgnore
    private User user;
}
