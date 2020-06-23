package engine.service;
import engine.model.Answer;
import engine.model.Quiz;
import engine.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz getQuizById(final long id) {
        return quizRepository.findById(id);
    }

    public Quiz addQuiz(Quiz quiz) {
        if (quiz.getAnswer() == null) {
            quiz.setAnswer(new ArrayList<>());
        }
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public boolean answerIsCorrect(final Answer answer, final Quiz quiz) {
        return answer.getAnswer().equals(quiz.getAnswer());
    }
}
