package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Идентификатор вещи не может быть пустым")
    private final Long itemId;

    /**
     * Дата начала бронирования.
     */
    @FutureOrPresent(message = "Дата начала бронирования не может быть меньше текущей")
    @NotNull(message = "Дата начала бронирования не может быть пустой")
    private final LocalDateTime start;

    /**
     * Дата окончания бронирования.
     */
    @Future(message = "Дата окончания бронирования не может быть меньше или равна текущей")
    @NotNull(message = "Дата окончания бронирования не может быть пустой")
    private final LocalDateTime end;
}
