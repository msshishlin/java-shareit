package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Трансферный объект для сущности "Запрос вещи".
 */
@Builder(toBuilder = true)
@Data
public final class ItemRequestDto {
    /**
     * Идентификатор запроса.
     */
    private final long id;

    /**
     * Описание требуемой вещи.
     */
    private final String description;

    /**
     * Дата и время создания запроса.
     */
    private final LocalDateTime created;
}
