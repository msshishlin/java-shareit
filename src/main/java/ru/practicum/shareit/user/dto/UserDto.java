package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Трансферный объект для сущности "Пользователь".
 */
@Builder(toBuilder = true)
@Data
public final class UserDto {
    /**
     * Идентификатор пользователя.
     */
    private final long id;

    /**
     * Имя пользователя.
     */
    private final String name;

    /**
     * Адрес электронной почты пользователя.
     */
    private final String email;
}
