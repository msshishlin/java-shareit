package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

/**
 * Пользователь.
 */
@Builder(toBuilder = true)
@Data
public final class User {
    /**
     * Идентификатор пользователя.
     */
    private long id;

    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * Адрес электронной почты пользователя.
     */
    private String email;
}
