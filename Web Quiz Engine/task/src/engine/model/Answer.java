package engine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Answer {
    private final boolean success;
    private String feedback;

    public Answer(boolean success) {
        this.success = success;
        setFeedback();
    }

    private void setFeedback() {
        feedback = success ? "Congratulations, you're right!"
                : "Wrong answer! Please, try again.";
    }
}
