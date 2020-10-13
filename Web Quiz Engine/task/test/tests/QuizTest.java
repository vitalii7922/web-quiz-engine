package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.controller.QuizController;
import engine.model.Quiz;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class QuizTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getQuizByIdTestAllLayers() throws Exception {
        Quiz quiz = Quiz.builder()
                .text("What plants are fruits?")
                .title("Fruits")
                .id(962L)
                .build();

        if (mockMvc != null) {
            mockMvc.perform(get("http://localhost:8889/api/quizzes/962"))
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(objectMapper.writeValueAsString(quiz)))
                    .andExpect(view().name("quiz"))
                    .andExpect(status().isOk());
        } else {
            System.out.println("doesn't work");
        }
    }
}


