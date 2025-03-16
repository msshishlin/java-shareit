package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

/**
 * Трансферный объект для запроса обновления пользователя.
 */
@Data
public final class UpdateUserDto {
    /**
     * Имя пользователя.
     */
    private final String name;

    /**
     * Адрес электронной почты пользователя.
     */
    @Email(message = "Адрес электронной почты должен содержать символ '@'")
    private final String email;
}
