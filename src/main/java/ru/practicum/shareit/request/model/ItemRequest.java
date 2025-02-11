package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Запрос вещи.
 */
@Builder(toBuilder = true)
@Data
public final class ItemRequest {
    /**
     * Идентификатор запроса.
     */
    private long id;

    /**
     * Описание требуемой вещи.
     */
    private String description;

    /**
     * Идентификатор пользователя, создавшего запрос.
     */
    private long requesterId;

    /**
     * Дата и время создания запроса.
     */
    private LocalDateTime createdAt;
}
