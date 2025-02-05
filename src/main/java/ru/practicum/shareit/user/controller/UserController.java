package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

/**
 * Контроллер для запросов пользователей.
 */
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    /**
     * Сервис для работы с пользователями.
     */
    private final UserService userService;

    /**
     * Создать пользователя.
     *
     * @param dto трансферный объект для запроса создания пользователя.
     * @return трансферный объект пользователя.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid CreateUserDto dto) {
        return UserMapper.mapToUserDto(userService.createUser(UserMapper.mapToUser(dto)));
    }

    /**
     * Получить пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return трансферный объект пользователя.
     */
    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return UserMapper.mapToUserDto(userService.getUserById(userId));
    }

    /**
     * Обновить пользователя.
     *
     * @param userId идентификатор пользователя.
     * @param dto    трансферный объект для запроса обновления пользователя.
     * @return трансферный объект пользователя.
     */
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody @Valid UpdateUserDto dto) {
        return UserMapper.mapToUserDto(userService.updateUser(UserMapper.mapToUser(userId, dto)));
    }

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
