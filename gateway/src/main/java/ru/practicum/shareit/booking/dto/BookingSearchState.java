package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingSearchState {
    /**
     * Все брони.
     */
    ALL,

    /**
     * Текущие брони.
     */
    CURRENT,

    /**
     * Завершенные брони.
     */
    PAST,

    /**
     * Будущие брони.
     */
    FUTURE,

    /**
     * Брони, ожидающие подтверждения.
     */
    WAITING,

    /**
     * Отклоненные брони.
     */
    REJECTED;

    public static Optional<BookingSearchState> from(String stringState) {
        for (BookingSearchState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
