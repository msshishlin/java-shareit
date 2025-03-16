package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;

/**
 * Контроллер для работы с запросами вещей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public final class ItemRequestController {
    /**
     * REST-клиент для работы с запросами вещей.
     */
    private final ItemRequestClient requestClient;

    /**
     * Создать запрос вещи.
     *
     * @param requesterId идентификатор пользователя, создавшего запрос.
     * @param dto         трансферный объект для создания запроса вещи.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createRequest(@RequestHeader(name = "X-Sharer-User-Id") long requesterId, @RequestBody @Valid CreateItemRequestDto dto) {
        return requestClient.createRequest(requesterId, dto);
    }

    /**
     * Получить список запросов вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return requestClient.getUserRequests(userId);
    }

    /**
     * Получить список запросов вещей других пользователей.
     *
     * @param userId идентификатор пользователя.
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsersRequests(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return requestClient.getOtherUsersRequests(userId);
    }

    /**
     * Получить запрос вещи по идентификатору.
     *
     * @param userId    идентификатор пользователя.
     * @param requestId идентификатор запроса вещи.
     * @return запрос вещи.
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(name = "X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        return requestClient.getRequest(userId, requestId);
    }
}
