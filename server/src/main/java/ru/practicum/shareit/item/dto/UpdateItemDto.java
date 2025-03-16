package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Трансферный объект для запроса обновления вещи.
 */
@Builder(toBuilder = true)
@Data
public final class UpdateItemDto {
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
}
