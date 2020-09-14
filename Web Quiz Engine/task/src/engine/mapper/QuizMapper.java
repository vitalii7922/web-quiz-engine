package engine.mapper;

import engine.dto.QuizDto;
import engine.model.Quiz;
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
                .title(quiz.getTitle())
                .text(quiz.getText())
                .options(quiz.getOptions())
                .build();
    }
}
