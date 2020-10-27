package com.engine.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Answer {
    private List<Integer> answers;

    Object object;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return Objects.equals(getAnswers(), answer.getAnswers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAnswers());
    }
}
