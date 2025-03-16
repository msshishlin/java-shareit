package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

/**
 * Тесты для контроллера для работы с пользователями.
 */
@WebMvcTest(controllers = UserController.class)
public final class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    private final CreateUserDto createUserDto = CreateUserDto.builder()
            .name("Иван Иванов")
            .email("ivanov@yandex.ru")
            .build();

    private final User user = User.builder()
            .id(1L)
            .name("Иван Иванов")
            .email("ivanov@yandex.ru")
            .build();

    private final UpdateUserDto updateUserDto = UpdateUserDto.builder()
            .name("Иван Иванов")
            .email("ivanov@yandex.ru")
            .build();

    @Test
    void createUserTest() throws Exception {
        Mockito.when(userService.createUser(Mockito.any()))
                .thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(mapper.writeValueAsString(createUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1L), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(user.getEmail())));
    }

    @Test
    void getUserByIdTest() throws Exception {
        Mockito.when(userService.getUserById(1L))
                .thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/users/" + 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(user.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(user.getEmail())));
    }

    @Test
    void updateUserTest() throws Exception {
        Mockito.when(userService.updateUser(Mockito.any()))
                .thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.patch("/users/" + 1)
                        .content(mapper.writeValueAsString(updateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1L), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(user.getEmail())));
    }

    @Test
    void deleteUserTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/users/" + 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
