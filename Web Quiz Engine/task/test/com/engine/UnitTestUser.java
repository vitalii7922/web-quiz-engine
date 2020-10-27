package com.engine;

import com.engine.dto.UserDto;
import com.engine.mapper.UserMapper;
import com.engine.model.User;
import com.engine.model.UserImpl;
import com.engine.repository.UserRepository;
import com.engine.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UnitTestUser {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    @Test
    void testAuthorizeUserResultBadRequest() throws Exception {
        User user = User.builder()
                .email("test1@gmail.com")
                .password("12345")
                .build();

        Mockito.when(userRepositoryMock.findByEmail("test1@gmail.com")).thenReturn(user);
        mockMvc.perform(post("/api/register")
                .content(objectMapper.writeValueAsString(userMapper.toUserDto(user)))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAuthorizeUserResultOk() throws Exception {
        User user = User.builder()
                .email("test1@gmail.com")
                .password("12345")
                .build();

        UserDto userDto = UserDto.builder()
                .email("test1@gmail.com")
                .password("12345")
                .build();

        Mockito.when(userRepositoryMock.findByEmail("test1@gmail.com")).thenReturn(null);
        Mockito.when(userRepositoryMock.save(Mockito.any(User.class))).thenReturn(user);
        mockMvc.perform(post("/api/register")
                .content(objectMapper.writeValueAsString(userDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testLoadUserByUsernameReturnUserImpl() {
        User user = User.builder()
                .email("test1@gmail.com")
                .password("12345")
                .build();

        Mockito.when(userRepositoryMock.findByEmail("test1@gmail.com")).thenReturn(user);
        Assert.assertEquals(new UserImpl(user), userService.loadUserByUsername("test1@gmail.com"));
    }


    @Test
    void testLoadUserByUsernameThrowException() {
        Mockito.when(userRepositoryMock.findByEmail("test1@gmail.com")).thenReturn(null);
        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userService.loadUserByUsername("test1@gmail.com"))
                .withMessage("Not found: test1@gmail.com");
    }

    @Test
    void getQuizResultOk() throws Exception {
        mockMvc.perform(get("/api/quizzes/1"))
                .andExpect(status().isUnauthorized());
    }

}
