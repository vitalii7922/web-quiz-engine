package engine.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CompletedQuizDto {
    private long id;

    private String title;

    private String text;

    private LocalDateTime completedAt;
}
