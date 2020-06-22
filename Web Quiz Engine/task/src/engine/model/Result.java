package engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public final class Result {
    private final boolean success;
    private String feedback;

    public static final Result SUCCESS_RESULT = new Result(true);
    public static final Result FAILURE_RESULT = new Result(false);

    public Result(boolean success) {
        this.success = success;
        setFeedback(success);
    }

    private void setFeedback(boolean success) {
        feedback = success ? "Congratulations, you're right!"
                : "Wrong answer! Please, try again.";
    }
}