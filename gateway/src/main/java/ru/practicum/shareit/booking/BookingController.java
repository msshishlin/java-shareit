package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingSearchState;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

/**
 * Контроллер для работы с бронью.
 */
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@RestController
public class BookingController {
    /**
     * REST-клиент для работы с бронями.
     */
    private final BookingClient bookingClient;

    /**
     * Создать бронь.
     *
     * @param bookerId идентификатор пользователя, желающего забронировать вещь.
     * @param dto      трансферный объект для запроса бронирования вещи.
     * @return трансферный объект для сущности "Бронь".
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestHeader(name = "X-Sharer-User-Id") long bookerId,
                                                @RequestBody @Valid CreateBookingDto dto) {
        return bookingClient.createBooking(bookerId, dto);
    }

    /**
     * Получить коллекцию броней пользователя.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @param state    состояние для поиска броней.
     * @param from     количество броней, которое надо пропустить.
     * @param size     количество броней, которое надо получить.
     * @return коллекция броней.
     */
    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                              @RequestParam(name = "state", defaultValue = "all") String state,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingSearchState searchState = BookingSearchState.from(state).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingClient.getBookings(bookerId, searchState, from, size);
    }

    /**
     * Получить коллекцию броней владельца вещей.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param state   состояние для поиска броней.
     * @return коллекция броней.
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(name = "X-Sharer-User-Id") long ownerId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingSearchState searchState = BookingSearchState.from(state).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingClient.getOwnerBookings(ownerId, searchState, from, size);
    }

    /**
     * Получить бронь.
     *
     * @param userId    идентификатор пользователя.
     * @param bookingId идентификатор брони.
     * @return бронь.
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
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
    public ResponseEntity<Object> approveBooking(@RequestHeader(name = "X-Sharer-User-Id") long ownerId,
                                                 @PathVariable long bookingId,
                                                 @RequestParam boolean approved) {
        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }
}
