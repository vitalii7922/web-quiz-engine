package com.engine.dto;

import com.engine.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizDto {

    private long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Description cannot be empty")
    private String text;

    @Size(min = 2, message = "There must be 2 options at least")
    @Size(max = 10, message = "You cannot add more than ten options")
    private List<String> options;

    @Size(min = 1, message = "There must be 1 correct answer at least")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> answer;

    private boolean modifiable;

    @JsonIgnore
    private User user;
}

