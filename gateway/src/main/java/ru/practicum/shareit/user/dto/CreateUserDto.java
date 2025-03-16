package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Трансферный объект для запроса создания пользователя.
 */
@Data
public final class CreateUserDto {
    /**
     * Имя пользователя.
     */
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private final String name;

    /**
     * Адрес электронной почты пользователя.
     */
    @Email(message = "Адрес электронной почты должен содержать символ '@'")
    @NotBlank(message = "Адрес электронной почты пользователя не может быть пустым")
    private final String email;
}
