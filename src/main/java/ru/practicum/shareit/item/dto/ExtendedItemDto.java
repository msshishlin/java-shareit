package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Расширенная версия трансферного объекта для сущности "Вещь".
 */
@Builder(toBuilder = true)
@Data
public final class ExtendedItemDto {
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
     * Дата окончания предыдущего бронирования.
     */
    private final LocalDateTime lastBooking;

    /**
     * Дата окончания следующего бронирования.
     */
    private final LocalDateTime nextBooking;
}
