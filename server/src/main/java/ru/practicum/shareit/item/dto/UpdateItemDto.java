package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * Трансферный объект для запроса обновления вещи.
 */
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
