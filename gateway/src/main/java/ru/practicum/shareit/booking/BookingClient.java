package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingSearchState;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

/**
 * REST-клиент для работы с бронями.
 */
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    /**
     * Конструктор.
     *
     * @param serverUrl адрес сервера.
     * @param builder
     */
    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Создать бронь.
     *
     * @param bookerId идентификатор пользователя, желающего забронировать вещь.
     * @param dto      трансферный объект для запроса бронирования вещи.
     * @return трансферный объект для сущности "Бронь".
     */
    public ResponseEntity<Object> createBooking(long bookerId, CreateBookingDto dto) {
        return post("", bookerId, dto);
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
    public ResponseEntity<Object> getBookings(long bookerId, BookingSearchState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );

        return get("?state={state}&from={from}&size={size}", bookerId, parameters);
    }

    /**
     * Получить коллекцию броней владельца вещей.
     *
     * @param bookerId идентификатор пользователя, осуществившего бронь.
     * @param state    состояние для поиска броней.
     * @param from     количество броней, которое надо пропустить.
     * @param size     количество броней, которое надо получить.
     * @return коллекция броней.
     */
    public ResponseEntity<Object> getOwnerBookings(long bookerId, BookingSearchState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );

        return get("/owner?state={state}&from={from}&size={size}", bookerId, parameters);
    }

    /**
     * Получить бронь.
     *
     * @param userId    идентификатор пользователя.
     * @param bookingId идентификатор брони.
     * @return бронь.
     */
    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    /**
     * Подтвердить/отклонить бронь.
     *
     * @param ownerId   идентификатор владельца вещи.
     * @param bookingId идентификатор бронирования.
     * @param approved  признак подтверждения брони.
     * @return бронь.
     */
    public ResponseEntity<Object> approveBooking(long ownerId, long bookingId, boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );

        return patch("/" + bookingId + "?approved={approved}", ownerId, parameters, null);
    }
}
