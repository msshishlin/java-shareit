package ru.practicum.shareit.booking.model;

/**
 * Состояние для поиска броней.
 */
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
    REJECTED,
}
