package engine.service;

import engine.controller.QuizController;
import engine.model.Answer;
import engine.model.Quiz;
import engine.model.UserImpl;
import engine.repository.QuizRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import engine.model.User;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    private final UserService userService;

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

    public Page<Quiz> getAllQuizzesByPageNumber(int pageNumber) {
        List<Quiz> quizList = quizRepository.findAll();
        if (!CollectionUtils.isEmpty(quizList)) {
            PageRequest page = PageRequest.of(pageNumber, 10, Sort.by("id"));
            return new PageImpl<>(quizRepository.findAll(page).getContent(), page, quizList.size());
        }
        return Page.empty();
    }

    public boolean answerIsCorrect(final Answer answer, final Quiz quiz) {
        return answer.getAnswer().equals(quiz.getAnswer());
    }

    public HttpStatus deleteQuizById(final long id) {
        Optional<Quiz> optionalQuiz = Optional.ofNullable(getQuizById(id));
        if (optionalQuiz.isPresent()) {
            User user = optionalQuiz.get().getUser();
            if (user != null
                    && user.getId() == userService.getUserImpl().getId()) {
                quizRepository.delete(optionalQuiz.get());
                return HttpStatus.NO_CONTENT;
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        String.format("You haven't created quiz with id %d", id));
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(QuizController.QUIZ_NOT_FOUND, id));
    }
}
