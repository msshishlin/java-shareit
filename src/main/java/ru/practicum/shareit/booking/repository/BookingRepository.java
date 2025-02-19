package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Контракт для хранилища броней.
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Получить список всех броней пользователя для конкретной вещи.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @param itemId   идентификатор вещи.
     * @return список броней.
     */
    List<Booking> findByBookerIdAndItemId(long bookerId, long itemId);

    /**
     * Получить список всех броней пользователя, отсортированных по убыванию даты начала бронирования.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @return список броней.
     */
    List<Booking> findByBookerIdOrderByStartDesc(long bookerId);

    /**
     * Получить список завершенных броней пользователя, отсортированных по убыванию даты начала бронирования.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @param before   дата, которая используется для сравнения с датой окончания бронирования.
     * @return список броней.
     */
    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime before);

    /**
     * Получить список текущих броней пользователя, отсортированных по убыванию даты начала бронирования.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @param after    дата, которая используется для сравнения с датой начала бронирования.
     * @param before   дата, которая используется для сравнения с датой окончания бронирования.
     * @return список броней.
     */
    List<Booking> findByBookerIdAndStartAfterAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime after, LocalDateTime before);

    /**
     * Получить список будущих броней пользователя, отсортированных по убыванию даты начала бронирования.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @param after    дата, которая используется для сравнения с датой начала бронирования.
     * @return список броней.
     */
    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime after);

    /**
     * Получить список броней пользователя в определенном статусе, отсортированных по убыванию даты начала бронирования.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @param status   статус брони.
     * @return список броней.
     */
    List<Booking> findByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);

    /**
     * Получить брони для вещи.
     *
     * @param itemId идентификатор вещи.
     * @return список броней.
     */
    List<Booking> findByItemId(long itemId);

    /**
     * Получить брони для коллекции вещей.
     *
     * @param items коллекция вещей.
     * @return список броней.
     */
    List<Booking> findByItemIn(Collection<Item> items);

    /**
     * Получить список всех броней владельца вещи, отсортированных по убыванию даты начала бронирования.
     *
     * @param ownerId идентификатор владельца вещи.
     * @return список броней.
     */
    List<Booking> findByItemOwnerIdOrderByStartDesc(long ownerId);

    /**
     * Получить список завершенных броней владельца вещи, отсортированных по убыванию даты начала бронирования.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param before  дата, которая используется для сравнения с датой окончания бронирования.
     * @return список броней.
     */
    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime before);

    /**
     * Получить список текущих броней владельца вещи, отсортированных по убыванию даты начала бронирования.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param after   дата, которая используется для сравнения с датой начала бронирования.
     * @param before  дата, которая используется для сравнения с датой окончания бронирования.
     * @return список броней.
     */
    List<Booking> findByItemOwnerIdAndStartAfterAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime after, LocalDateTime before);

    /**
     * Получить список будущих броней владельца вещи, отсортированных по убыванию даты начала бронирования.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param after   дата, которая используется для сравнения с датой начала бронирования.
     * @return список броней.
     */
    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime after);

    /**
     * Получить список броней владельца вещи в определенном статусе, отсортированных по убыванию даты начала бронирования.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param status  статус брони.
     * @return список броней.
     */
    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus status);
}
