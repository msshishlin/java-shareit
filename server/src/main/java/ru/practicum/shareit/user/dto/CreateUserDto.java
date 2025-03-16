package ru.practicum.shareit.user.dto;

import lombok.Data;

/**
 * Трансферный объект для запроса создания пользователя.
 */
@Data
public final class CreateUserDto {
    /**
     * Имя пользователя.
     */
    private final String name;

    /**
     * Адрес электронной почты пользователя.
     */
    private final String email;
}
