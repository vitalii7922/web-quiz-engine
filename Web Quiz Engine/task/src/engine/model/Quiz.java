package engine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Quiz {
    private String title;
    private String text;
    private List<String> options;
}
