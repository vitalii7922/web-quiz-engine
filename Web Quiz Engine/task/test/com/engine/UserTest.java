package com.engine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void auth() throws Exception {
        mockMvc.perform(post("/login")
                .param("email", "test2@gmail.com")
                .param("password", "12345"))
                .andExpect(status().isOk());
    }
}
