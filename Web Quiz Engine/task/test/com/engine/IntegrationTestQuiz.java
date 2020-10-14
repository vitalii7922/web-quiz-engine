package com.engine;

import com.engine.model.Quiz;
import com.engine.model.User;
import com.engine.repository.QuizRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username="test2@gmail.com", password="12345", authorities = {"USER"})
class IntegrationTestQuiz {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    QuizRepository quizRepository;

    @Before
    public void init() {
//        Mockito.when(quizRepository.findById(1L)).thenReturn(Optional.of(book));
    }


    @Test
    void auth() throws Exception {
        mockMvc.perform(post("/login")
                .param("email", "test2@gmail.com")
                .param("password", "12345"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        Quiz quiz = Quiz.builder()
                .text("What plants are fruits?")
                .title("Fruits")
                .id(962L)
                .build();

        mockMvc.perform(get("/api/quizzes/962"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(quiz)))
                .andExpect(view().name("quiz"))
                .andExpect(status().isOk());
    }
}
