package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Трансферный объект для запроса обновления пользователя.
 */
@Builder(toBuilder = true)
@Data
public final class UpdateUserDto {
    /**
     * Имя пользователя.
     */
    private final String name;

    /**
     * Адрес электронной почты пользователя.
     */
    private final String email;
}
