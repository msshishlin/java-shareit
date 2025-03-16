package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Трансферный объект для запроса бронирования вещи.
 */
@Builder(toBuilder = true)
@Data
public final class CreateBookingDto {
    /**
     * Идентификатор забронированной вещи.
     */
    private final Long itemId;

    /**
     * Дата начала бронирования.
     */
    private final LocalDateTime start;

    /**
     * Дата окончания бронирования.
     */
    private final LocalDateTime end;
}
