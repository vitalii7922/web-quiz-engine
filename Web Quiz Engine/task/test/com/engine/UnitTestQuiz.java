package com.engine;

import com.engine.dto.Answer;
import com.engine.dto.CompletedQuizDto;
import com.engine.dto.QuizDto;
import com.engine.dto.Result;
import com.engine.mapper.QuizMapper;
import com.engine.model.CompletedQuiz;
import com.engine.model.Quiz;
import com.engine.model.User;
import com.engine.repository.CompletedQuizRepository;
import com.engine.repository.QuizRepository;
import com.engine.service.UserService;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test2@gmail.com", password = "12345", authorities = {"USER"})
@ActiveProfiles("test")
class UnitTestQuiz {

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

    @MockBean
    CompletedQuizRepository completedQuizRepositoryMock;


    @BeforeEach
    void disableJsonAnnotation() {
        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
    }

    @Test
    void getQuizResultOk() throws Exception {
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
    void getQuizResultNotFound() throws Exception {
        mockMvc.perform(get("/api/quizzes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postQuizResultOk() throws Exception {
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

//        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);

        mockMvc.perform(post("/api/quizzes").content(objectMapper.writeValueAsString(quizDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(Collections.singletonList("Title cannot be empty"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void solveQuizResultOkCorrectAnswer() throws Exception {
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

//        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        Mockito.when(quizRepositoryMock.findById(1L)).thenReturn(quizMapper.toQuiz(quizDto));

        mockMvc.perform(post("/api/quizzes/1/solve").content(objectMapper.writeValueAsString(answer))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk());
    }

    @Test
    void solveQuizResultOkIncorrectAnswer() throws Exception {
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

//        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        Mockito.when(quizRepositoryMock.findById(1L)).thenReturn(quizMapper.toQuiz(quizDto));

        mockMvc.perform(post("/api/quizzes/1/solve").content(objectMapper.writeValueAsString(answer))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllQuizzesResultTwoElementsOnePage() throws Exception {
        User user1 = User.builder()
                .id(1L)
                .email("test2@gmail.com")
                .password("12345")
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("test3@gmail.com")
                .password("12345")
                .build();

        Quiz quiz1 = Quiz.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Banana", "Apple", "Beet", "Melon"))
                .answer(Arrays.asList(0, 1, 3))
                .user(user1)
                .build();

        Quiz quiz2 = Quiz.builder()
                .id(2L)
                .title("Cities")
                .text("What cities are capitals?")
                .options(Arrays.asList("London", "Berlin", "Moscow", "Manchester"))
                .answer(Arrays.asList(0, 1, 2))
                .user(user2)
                .build();


        PageRequest page = PageRequest.of(0, 5, Sort.by("id"));
        List<Quiz> quizList = Arrays.asList(quiz1, quiz2);
        QuizDto quizDto1 = quizMapper.toQuizDto(quiz1);
        quizDto1.setModifiable(true);
        QuizDto quizDto2 = quizMapper.toQuizDto(quiz2);
        quizDto2.setModifiable(true);
        when(quizRepositoryMock.countAll()).thenReturn(2L);
        when(quizRepositoryMock.findAll(page)).thenReturn(new PageImpl<>(quizList, page, 2));
        when(userServiceMock.getCurrentUser()).thenReturn(user1).thenReturn(user2);
//        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        mockMvc.perform(get("/api/quizzes?page=1"))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Fruits")))
                .andExpect(jsonPath("$.content[0].modifiable", is(true)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].title", is("Cities")))
                .andExpect(jsonPath("$.content[1].modifiable", is(true)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.numberOfElements", is(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllQuizzesResultEmptyList() throws Exception {
        when(quizRepositoryMock.countAll()).thenReturn(0L);
        mockMvc.perform(get("/api/quizzes?page=1"))
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteQuizNotFound() throws Exception {
        when(quizRepositoryMock.findById(1L)).thenReturn(null);
        mockMvc.perform(delete("/api/quizzes/1")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void updateQuizTestQuizNotFound() throws Exception {
        QuizDto quizDto = QuizDto.builder()
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Apple", "Cucumber", "Onion", "Tomato"))
                .answer(Collections.singletonList(0))
                .build();

        when(quizRepositoryMock.findById(1L)).thenReturn(null);
        mockMvc.perform(put("/api/quizzes/1").content(objectMapper.writeValueAsString(quizDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateQuizTestQuizForbidden() throws Exception {
        User user1 = User.builder()
                .id(1L)
                .email("test2@gmail.com")
                .password("12345")
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("test3@gmail.com")
                .password("12345")
                .build();

        QuizDto quizDto = QuizDto.builder()
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Apple", "Cucumber", "Onion", "Tomato"))
                .answer(Collections.singletonList(0))
                .build();

        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Apple", "Cucumber", "Onion", "Tomato"))
                .answer(Collections.singletonList(0))
                .user(user1)
                .build();

        when(quizRepositoryMock.findById(1L)).thenReturn(quiz);
        when(userServiceMock.getCurrentUser()).thenReturn(user2);
        mockMvc.perform(put("/api/quizzes/1").content(objectMapper.writeValueAsString(quizDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateQuizTestResultOk() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("test3@gmail.com")
                .password("12345")
                .build();

        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Banana", "Apple", "Beet", "Melon"))
                .answer(Arrays.asList(0, 1, 3))
                .user(user)
                .build();


        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Apple", "Cucumber", "Onion", "Tomato"))
                .answer(Collections.singletonList(0))
                .user(user)
                .build();



        when(quizRepositoryMock.findById(1L)).thenReturn(quiz);
        when(userServiceMock.getCurrentUser()).thenReturn(user);
        quiz.setOptions(quizDto.getOptions());
        quiz.setAnswer(quizDto.getAnswer());
        Mockito.when(quizRepositoryMock.save(any(Quiz.class))).thenReturn(quizMapper.toQuiz(quizDto));
        mockMvc.perform(put("/api/quizzes/1").content(objectMapper.writeValueAsString(quizDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.options[0]", is("Apple")))
                .andExpect(jsonPath("$.options[1]", is("Cucumber")))
                .andExpect(jsonPath("$.options[2]", is("Onion")))
                .andExpect(jsonPath("$.options[3]", is("Tomato")))
                .andExpect(jsonPath("$.answer[0]", is(0)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteQuizForbidden() throws Exception {
        User user1 = User.builder()
                .id(1L)
                .email("test2@gmail.com")
                .password("12345")
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("test3@gmail.com")
                .password("12345")
                .build();

        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Apple", "Cucumber", "Onion", "Tomato"))
                .answer(Collections.singletonList(0))
                .user(user1)
                .build();

        when(quizRepositoryMock.findById(1L)).thenReturn(quiz);
        when(userServiceMock.getCurrentUser()).thenReturn(user2);
        mockMvc.perform(delete("/api/quizzes/1")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteQuizNoContent() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("test2@gmail.com")
                .password("12345")
                .build();

        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .options(Arrays.asList("Apple", "Cucumber", "Onion", "Tomato"))
                .answer(Collections.singletonList(0))
                .user(user)
                .build();

        when(quizRepositoryMock.findById(1L)).thenReturn(quiz);
        when(userServiceMock.getCurrentUser()).thenReturn(user);
        mockMvc.perform(delete("/api/quizzes/1")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getCompletedQuizzesResultTwoElements() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("test2@gmail.com")
                .password("12345")
                .build();

        CompletedQuiz completedQuiz1 = CompletedQuiz.builder()
                .id(1L)
                .title("Fruits")
                .text("What plants are fruits?")
                .user(user)
                .completedAt(LocalDateTime.of(LocalDate.of(2020, 10, 22),
                        LocalTime.of(10, 10)))
                .build();

        CompletedQuiz completedQuiz2 = CompletedQuiz.builder()
                .id(2L)
                .title("Cities")
                .text("What cities are capitals?")
                .user(user)
                .completedAt(LocalDateTime.of(LocalDate.of(2020, 10, 21),
                        LocalTime.of(10, 10)))
                .build();

        objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
        when(userServiceMock.getCurrentUser()).thenReturn(user);
        when(completedQuizRepositoryMock.countAllByUserId(1L)).thenReturn(2L);
        PageRequest page = PageRequest.of(0, 5, Sort.by("completedAt").descending());
        when(completedQuizRepositoryMock.findAllByUserId(1L, page))
                .thenReturn(new PageImpl<>(Arrays.asList(completedQuiz1, completedQuiz2)));
        mockMvc.perform(get("/api/quizzes/completed?page=1"))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Fruits")))
                .andExpect(jsonPath("$.content[0].completedAt", is("22-10-2020 10:10")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].title", is("Cities")))
                .andExpect(jsonPath("$.content[1].completedAt", is("21-10-2020 10:10")))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(status().isOk());
    }


    @Test
    void getCompletedQuizzesPagesNumberTwoResultEmptyContent() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("test2@gmail.com")
                .password("12345")
                .build();

        objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
        when(userServiceMock.getCurrentUser()).thenReturn(user);
        when(completedQuizRepositoryMock.countAllByUserId(1L)).thenReturn(2L);
        PageRequest page = PageRequest.of(1, 5, Sort.by("completedAt").descending());
        when(completedQuizRepositoryMock.findAllByUserId(1L, page))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        mockMvc.perform(get("/api/quizzes/completed?page=2"))
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.numberOfElements", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllQuizzesCompletedResultEmptyList() throws Exception {
        User user = User.builder()
                .id(2L)
                .email("test2@gmail.com")
                .password("12345")
                .build();

        when(userServiceMock.getCurrentUser()).thenReturn(user);
        when(completedQuizRepositoryMock.countAllByUserId(2L)).thenReturn(0L);
        mockMvc.perform(get("/api/quizzes/completed?page=1"))
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
