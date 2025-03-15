package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

/**
 * Контроллер для работы с запросами вещей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public final class ItemRequestController {
    /**
     * Сервис для работы с запросами вещей.
     */
    private final ItemRequestService requestService;

    /**
     * Создать запрос вещи.
     *
     * @param requesterId идентификатор пользователя, создавшего запрос.
     * @param dto         трансферный объект для создания запроса вещи.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createRequest(@RequestHeader(name = "X-Sharer-User-Id") long requesterId, @RequestBody @Valid CreateItemRequestDto dto) {
        return ItemRequestMapper.mapToItemRequestDto(requestService.createItemRequest(ItemRequestMapper.mapToItemRequest(requesterId, dto)));
    }

    /**
     * Получить список запросов вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    @GetMapping
    public Collection<ItemRequestDto> getUserRequests(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return ItemRequestMapper.mapToItemRequestDtoCollection(requestService.getUserItemRequests(userId));
    }

    /**
     * Получить список запросов вещей других пользователей.
     *
     * @param userId идентификатор пользователя.
     */
    @GetMapping("/all")
    public Collection<ItemRequestDto> getOtherUsersRequests(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return ItemRequestMapper.mapToItemRequestDtoCollection(requestService.getOtherUsersItemRequests(userId));
    }

    /**
     * Получить запрос вещи по идентификатору.
     *
     * @param userId    идентификатор пользователя.
     * @param requestId идентификатор запроса вещи.
     * @return запрос вещи.
     */
    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto getRequest(@RequestHeader(name = "X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        return requestService.getRequest(userId, requestId);
    }
}
