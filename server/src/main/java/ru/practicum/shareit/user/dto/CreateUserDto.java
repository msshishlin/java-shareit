package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Трансферный объект для запроса создания пользователя.
 */
@Builder(toBuilder = true)
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
