package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Сервис для работы с бронями.
 */
@RequiredArgsConstructor
@Service
public final class BookingServiceImpl implements BookingService {
    /**
     * Хранилище броней.
     */
    private final BookingRepository bookingRepository;

    /**
     * Хранилище вещей.
     */
    private final ItemRepository itemRepository;

    /**
     * Хранилище пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Создать бронь.
     *
     * @param booking бронь.
     * @return бронь.
     */
    @Override
    public Booking createBooking(Booking booking) {
        User booker = userRepository.findById(booking.getBooker().getId()).orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", booking.getBooker().getId())));

        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найдена", booking.getItem().getId())));
        if (!item.getAvailable()) {
            throw new BookingException(String.format("Вещь с id = %d недоступна для бронирования", booking.getItem().getId()));
        }

        booking.setBooker(booker);
        booking.setItem(item);

        return bookingRepository.save(booking);
    }

    /**
     * Получить бронь.
     *
     * @param bookingId идентификатор брони.
     * @param userId    идентификатор пользователя.
     * @return бронь.
     */
    @Override
    public Booking getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format("Бронь с id = %d не найдена", bookingId)));

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new AccessDeniedException(String.format("Доступ к брони с id = %d запрещен", bookingId));
        }

        return booking;
    }

    /**
     * Получить коллекцию броней пользователя в определенном состоянии.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @param state    состояние для поиска броней.
     * @return коллекция броней.
     */
    @Override
    public Collection<Booking> getBookings(long bookerId, BookingSearchState state) {
        if (!userRepository.existsById(bookerId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", bookerId));
        }

        return switch (state) {
            case ALL -> bookingRepository.findByBookerIdOrderByStartDesc(bookerId);
            case PAST -> bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
            case CURRENT ->
                    bookingRepository.findByBookerIdAndStartAfterAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
        };
    }

    /**
     * Получить коллекцию броней владельца вещи в определенном состоянии.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param state   состояние для поиска броней.
     * @return коллекция броней.
     */
    @Override
    public Collection<Booking> getOwnerBookings(long ownerId, BookingSearchState state) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", ownerId));
        }

        return switch (state) {
            case ALL -> bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
            case PAST -> bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
            case FUTURE ->
                    bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now());
            case CURRENT ->
                    bookingRepository.findByItemOwnerIdAndStartAfterAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING ->
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
        };
    }

    /**
     * Подтвердить/отклонить бронь.
     *
     * @param bookingId идентификатор бронь.
     * @param ownerId   идентификатор владельца вещи.
     * @param approved  признак подтверждения брони.
     * @return бронь.
     */
    @Override
    public Booking approveBooking(long bookingId, long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format("Бронь с id = %d не найдена", bookingId)));

        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new AccessDeniedException(String.format("Подтверждение брони с id = %d пользователем с id = %d запрещено", bookingId, ownerId));
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return bookingRepository.save(booking);
    }
}
