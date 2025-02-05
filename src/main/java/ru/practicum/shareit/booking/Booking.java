package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

/**
 * Бронь.
 */
@Data
public final class Booking {

    /**
     * Вещь, на которую действует бронь.
     */
    private final Item item;

    /**
     * Дата начала бронирования.
     */
    private final LocalDate startDate;

    /**
     * Дата окончания бронирования.
     */
    private final LocalDate endDate;

    /**
     * Признак, что бронь подтверждена владельцем вещи.
     */
    private final boolean isAcceptedByOwner;
}
