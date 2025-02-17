package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDate;

/**
 * Трансферный объект для сущности "Бронь".
 */
@Builder(toBuilder = true)
@Data
public final class BookingDto {
    /**
     * Идентификатор брони.
     */
    private final long id;

    /**
     * Дата начала бронирования.
     */
    private final LocalDate startDate;

    /**
     * Дата окончания бронирования.
     */
    private final LocalDate endDate;

    /**
     * Идентификатор забронированной вещи.
     */
    private final long itemId;

    /**
     * Идентификатор пользователя, осуществившего бронь.
     */
    private final boolean bookerId;

    /**
     * Статус брони.
     */
    private final BookingStatus status;
}
