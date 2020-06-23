package engine.service;

import engine.model.Answer;
import engine.model.Quiz;
import engine.model.UserImpl;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import engine.model.User;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    private final UserService userService;
    @Autowired

    public QuizService(QuizRepository quizRepository, UserService userService) {
        this.quizRepository = quizRepository;
        this.userService = userService;
    }


    public Quiz getQuizById(final long id) {
        return quizRepository.findById(id);
    }

    public Quiz addQuiz(Quiz quiz) {
        UserImpl userImpl = userService.getUserImpl();
        User user = User.builder()
                .id(userImpl.getId())
                .email(userImpl.getUsername())
                .password(userImpl.getPassword())
                .build();
        quiz.setUser(user);
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

    public HttpStatus deleteQuizById(final long id) {
        Optional<Quiz> optionalQuiz = Optional.ofNullable(getQuizById(id));
        if (optionalQuiz.isPresent()) {
            if (optionalQuiz.get().getUser() != null
                    && optionalQuiz.get().getUser().getId() == userService.getUserImpl().getId()) {
                quizRepository.delete(optionalQuiz.get());
                return HttpStatus.NO_CONTENT;
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        return HttpStatus.BAD_REQUEST;
    }
}
