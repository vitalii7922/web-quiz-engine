package com.engine.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ApiError {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private List<String> message;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }


    public ApiError(HttpStatus status, List<String> messages) {
        this();
        this.status = status;
        this.message = messages;
    }
}
