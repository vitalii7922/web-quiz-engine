package com.engine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private LocalDateTime completedAt;
}
