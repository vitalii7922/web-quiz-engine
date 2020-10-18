package com.engine.mapper;

import com.engine.model.Quiz;
import com.engine.dto.CompletedQuizDto;
import com.engine.dto.QuizDto;
import com.engine.model.CompletedQuiz;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class QuizMapper {
    public Quiz toQuiz(QuizDto quizDto) {
        if (quizDto.getAnswer() == null) {
            quizDto.setAnswer(new ArrayList<>());
        }
        return Quiz.builder()
                .title(quizDto.getTitle())
                .text(quizDto.getText())
                .options(quizDto.getOptions())
                .answer(quizDto.getAnswer())
                .build();
    }

    public QuizDto toQuizDto(Quiz quiz) {
        return QuizDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .text(quiz.getText())
                .user(quiz.getUser())
                .options(quiz.getOptions())
                .answer(quiz.getAnswer())
                .build();
    }

    public CompletedQuizDto toCompletedQuizDto(CompletedQuiz completedQuiz) {
        return CompletedQuizDto.builder()
                .id(completedQuiz.getId())
                .title(completedQuiz.getTitle())
                .text(completedQuiz.getText())
                .completedAt(completedQuiz.getCompletedAt())
                .build();
    }
}
