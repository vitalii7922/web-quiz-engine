package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    long id;
    private String title;
    private String text;
    private List<String> options;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int answer;
}
