package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Трансферный объект для запроса создания вещи.
 */
@Builder(toBuilder = true)
@Data
public final class CreateItemDto {
    /**
     * Название вещи.
     */
    private final String name;

    /**
     * Описание вещи.
     */
    private final String description;

    /**
     * Признак доступности вещи для аренды.
     */
    private final Boolean available;

    /**
     * Идентификатор запроса вещи.
     */
    private final Long requestId;
}
