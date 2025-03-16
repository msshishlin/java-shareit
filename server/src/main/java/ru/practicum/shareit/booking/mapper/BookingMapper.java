package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

/**
 * Маппер для моделей бронирования.
 */
public final class BookingMapper {
    /**
     * Преобразовать трансферный объект для запроса бронирования вещи в объект брони.
     *
     * @param bookerId идентификатор пользователя, желающего забронировать вещь.
     * @param dto      трансферный объект для запроса бронирования вещи.
     * @return бронь.
     */
    public static Booking mapToBooking(long bookerId, CreateBookingDto dto) {
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .status(BookingStatus.WAITING)
                .booker(User.builder().id(bookerId).build())
                .item(Item.builder().id(dto.getItemId()).build())
                .build();
    }

    /**
     * Преобразовать объект брони в трансферный объект для сущности "Бронь".
     *
     * @param booking бронь.
     * @return трансферный объект для сущности "Бронь".
     */
    public static BookingDto mapToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(UserMapper.mapToUserDto(booking.getBooker()))
                .item(ItemMapper.mapToItemDto(booking.getItem()))
                .build();

    }

    /**
     * Преобразовать коллекцию объектов брони в коллекцию трансферных объектов для сущности "Бронь".
     *
     * @param bookings коллекция броней.
     * @return коллекция трансферных объектов для сущности "Бронь".
     */
    public static Collection<BookingDto> mapToBookingDtoCollection(Collection<Booking> bookings) {
        return bookings.stream().map(BookingMapper::mapToBookingDto).toList();
    }
}
