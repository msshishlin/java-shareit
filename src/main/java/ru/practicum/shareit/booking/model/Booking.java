package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Бронь.
 */
@Builder(toBuilder = true)
@Data
public final class Booking {
    /**
     * Идентификатор брони.
     */
    private long id;

    /**
     * Дата начала бронирования.
     */
    private LocalDate startDate;

    /**
     * Дата окончания бронирования.
     */
    private LocalDate endDate;

    /**
     * Идентификатор забронированной вещи.
     */
    private long itemId;

    /**
     * Идентификатор пользователя, осуществившего бронь.
     */
    private boolean bookerId;

    /**
     * Статус брони.
     */
    private BookingStatus status;
}
