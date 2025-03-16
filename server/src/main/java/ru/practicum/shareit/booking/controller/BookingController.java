package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingSearchState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

/**
 * Контроллер для работы с бронью.
 */
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@RestController
public final class BookingController {
    /**
     * Сервис для работы с бронями.
     */
    private final BookingService bookingService;

    /**
     * Создать бронь.
     *
     * @param bookerId идентификатор пользователя, желающего забронировать вещь.
     * @param dto      трансферный объект для запроса бронирования вещи.
     * @return трансферный объект для сущности "Бронь".
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader(name = "X-Sharer-User-Id") long bookerId,
                                    @RequestBody CreateBookingDto dto) {
        return BookingMapper.mapToBookingDto(bookingService.createBooking(BookingMapper.mapToBooking(bookerId, dto)));
    }

    /**
     * Получить бронь.
     *
     * @param userId    идентификатор пользователя.
     * @param bookingId идентификатор брони.
     * @return бронь.
     */
    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                 @PathVariable long bookingId) {
        return BookingMapper.mapToBookingDto(bookingService.getBooking(bookingId, userId));
    }

    /**
     * Получить коллекцию броней пользователя.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @param state    состояние для поиска броней.
     * @return коллекция броней.
     */
    @GetMapping
    public Collection<BookingDto> getBookings(@RequestHeader(name = "X-Sharer-User-Id") long bookerId,
                                              @RequestParam(defaultValue = "ALL") BookingSearchState state) {
        return BookingMapper.mapToBookingDtoCollection(bookingService.getBookings(bookerId, state));
    }

    /**
     * Получить коллекцию броней владельца вещей.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param state   состояние для поиска броней.
     * @return коллекция броней.
     */
    @GetMapping("/owner")
    public Collection<BookingDto> getOwnerBookings(@RequestHeader(name = "X-Sharer-User-Id") long ownerId,
                                                   @RequestParam(defaultValue = "ALL") BookingSearchState state) {
        return BookingMapper.mapToBookingDtoCollection(bookingService.getOwnerBookings(ownerId, state));
    }

    /**
     * Подтвердить/отклонить бронь.
     *
     * @param ownerId   идентификатор владельца вещи.
     * @param bookingId идентификатор бронирования.
     * @param approved  признак подтверждения брони.
     * @return бронь.
     */
    @PatchMapping(value = "/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(name = "X-Sharer-User-Id") long ownerId,
                                     @PathVariable long bookingId,
                                     @RequestParam boolean approved) {
        return BookingMapper.mapToBookingDto(bookingService.approveBooking(bookingId, ownerId, approved));
    }
}
