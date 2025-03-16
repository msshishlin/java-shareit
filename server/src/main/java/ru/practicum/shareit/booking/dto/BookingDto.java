package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

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
    private final LocalDateTime start;

    /**
     * Дата окончания бронирования.
     */
    private final LocalDateTime end;

    /**
     * Статус брони.
     */
    private final BookingStatus status;

    /**
     * Идентификатор пользователя, осуществившего бронь.
     */
    private final UserDto booker;

    /**
     * Идентификатор забронированной вещи.
     */
    private final ItemDto item;
}
