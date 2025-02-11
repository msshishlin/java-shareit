package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

/**
 * Вещь.
 */
@Builder(toBuilder = true)
@Data
public final class Item {
    /**
     * Идентификатор вещи.
     */
    private long id;

    /**
     * Название вещи.
     */
    private String name;

    /**
     * Описание вещи.
     */
    private String description;

    /**
     * Признак доступности вещи для аренды.
     */
    private Boolean available;

    /**
     * Идентификатор владельца вещи.
     */
    private long ownerId;
}
