package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchState;

import java.util.Collection;

/**
 * Контракт сервиса для работы с бронями.
 */
public interface BookingService {
    /**
     * Создать бронь.
     *
     * @param booking бронь.
     * @return бронь.
     */
    Booking createBooking(Booking booking);

    /**
     * Получить бронь.
     *
     * @param bookingId идентификатор брони.
     * @param userId    идентификатор пользователя.
     * @return бронь.
     */
    Booking getBooking(long bookingId, long userId);

    /**
     * Получить коллекцию броней пользователя в определенном состоянии.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @param state    состояние для поиска броней.
     * @return коллекция броней.
     */
    Collection<Booking> getBookings(long bookerId, BookingSearchState state);

    /**
     * Получить коллекцию броней владельца вещи в определенном состоянии.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param state   состояние для поиска броней.
     * @return коллекция броней.
     */
    Collection<Booking> getOwnerBookings(long ownerId, BookingSearchState state);

    /**
     * Подтвердить/отклонить бронь.
     *
     * @param bookingId идентификатор бронь.
     * @param ownerId   идентификатор владельца вещи.
     * @param approved  признак подтверждения брони.
     * @return бронь.
     */
    Booking approveBooking(long bookingId, long ownerId, boolean approved);
}
