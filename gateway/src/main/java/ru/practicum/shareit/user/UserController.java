package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

/**
 * Контроллер для запросов пользователей.
 */
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@RestController
public final class UserController {
    /**
     * REST-клиент для работы с пользователями.
     */
    private final UserClient userClient;

    /**
     * Создать пользователя.
     *
     * @param dto трансферный объект для запроса создания пользователя.
     * @return трансферный объект пользователя.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createUser(@RequestBody @Valid CreateUserDto dto) {
        return userClient.createUser(dto);
    }

    /**
     * Получить пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return трансферный объект пользователя.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        return userClient.getUserById(userId);
    }

    /**
     * Обновить пользователя.
     *
     * @param userId идентификатор пользователя.
     * @param dto    трансферный объект для запроса обновления пользователя.
     * @return трансферный объект пользователя.
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable long userId,
                                             @RequestBody @Valid UpdateUserDto dto) {
        return userClient.updateUser(userId, dto);
    }

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userClient.deleteUser(userId);
    }
}
