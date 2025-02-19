package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

/**
 * Трансферный объект для сущности "Вещь".
 */
@Builder(toBuilder = true)
@Data
public final class ItemDto {
    /**
     * Идентификатор вещи.
     */
    private final long id;

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
     * Комментарии к вещи.
     */
    private Collection<String> comments;
}
