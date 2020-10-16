package com.engine;
import com.engine.dto.Answer;
import com.engine.dto.QuizDto;
import com.engine.dto.Result;
import com.engine.mapper.QuizMapper;
import com.engine.model.Quiz;
import com.engine.model.User;
import com.engine.repository.QuizRepository;
import com.engine.service.UserService;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test2@gmail.com", password = "12345", authorities = {"USER"})
class IntegrationTestQuiz {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    QuizRepository quizRepositoryMock;

    @Autowired
    QuizMapper quizMapper;

    @MockBean
    UserService userServiceMock;


    @Test
    void getQuizPositive() throws Exception {
        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Banana", "Apple", "Beet", "Melon"))
                .answer(Arrays.asList(0, 1, 3))
                .build();
        Mockito.when(quizRepositoryMock.findById(1L)).thenReturn(quiz);
        mockMvc.perform(get("/api/quizzes/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(quizMapper.toQuizDto(quiz))))
                .andExpect(status().isOk());
    }

    @Test
    void getQuizNegative() throws Exception {
        mockMvc.perform(get("/api/quizzes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postQuizPositive() throws Exception {
        User user = User.builder()
                .email("test2@gmail.com")
                .password("12345").build();


        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Banana", "Apple", "Beet", "Melon"))
                .answer(Arrays.asList(0, 1, 3))
                .user(user)
                .build();

        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        Mockito.when(userServiceMock.getCurrentUser()).thenReturn(user);
        Mockito.when(quizRepositoryMock.save(any(Quiz.class))).thenReturn(quizMapper.toQuiz(quizDto));
        mockMvc.perform(post("/api/quizzes").content(objectMapper.writeValueAsString(quizDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(quizDto)))
                .andExpect(status().isOk());
    }

    @Test
    void postQuizNegativeTitleBlank() throws Exception {
        QuizDto quizDto = QuizDto.builder()
                .title("   ")
                .text("What plants are fruits")
                .options(Arrays.asList("Banana", "Apple", "Beet", "Melon"))
                .answer(Arrays.asList(0, 1, 3))
                .build();

        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        mockMvc.perform(post("/api/quizzes").content(objectMapper.writeValueAsString(quizDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(Collections.singletonList("Title cannot be empty"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void solveQuizPositive() throws Exception {
        Answer answer = new Answer();
        answer.setAnswers(Arrays.asList(0, 1, 3));

        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Banana", "Apple", "Beet", "Melon"))
                .answer(Arrays.asList(0, 1, 3))
                .build();

        Result result = Result.SUCCESS_RESULT;

        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        Mockito.when(quizRepositoryMock.findById(1L)).thenReturn(quizMapper.toQuiz(quizDto));
        mockMvc.perform(post("/api/quizzes/1/solve").content(objectMapper.writeValueAsString(answer))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk());
    }

    @Test
    void solveQuizNegative() throws Exception {
        Answer answer = new Answer();
        answer.setAnswers(Arrays.asList(0, 3));

        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Banana", "Apple", "Beet", "Melon"))
                .answer(Arrays.asList(0, 1, 3))
                .build();

        Result result = Result.FAILURE_RESULT;

        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        Mockito.when(quizRepositoryMock.findById(1L)).thenReturn(quizMapper.toQuiz(quizDto));
        mockMvc.perform(post("/api/quizzes/1/solve").content(objectMapper.writeValueAsString(answer))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllQuizzesPositive() throws Exception {
        QuizDto quizDto1 = QuizDto.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Banana", "Apple", "Beet", "Melon"))
                .answer(Arrays.asList(0, 1, 3))
                .build();

        QuizDto quizDto2 = QuizDto.builder()
                .id(2L)
                .title("Cities")
                .text("What cities are capitals?")
                .options(Arrays.asList("London", "Berlin", "Moscow", "Manchester"))
                .answer(Arrays.asList(0, 1, 2))
                .build();

        List<Quiz> quizList = Arrays.asList(quizMapper.toQuiz(quizDto1), quizMapper.toQuiz(quizDto2));

        Mockito.when(quizRepositoryMock.findAll()).thenReturn(quizList);
        mockMvc.perform(get("/api/quizzes?page=1"))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
