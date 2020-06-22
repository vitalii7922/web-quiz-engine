package engine.model;
import lombok.Getter;

@Getter
public final class Result {
    private Boolean success;
    private String feedback;

    private Result(Boolean success) {
        this.success = success;
        setFeedBack(success);
    }

    public static Result fail() {
        return new Result(false);
    }

    public static Result success() {
        return new Result(true);
    }

    private void setFeedBack(boolean success) {
        feedback = success ?  "Congratulations, you're right!"
                : "Wrong answer! Please, try again.";
    }
}
