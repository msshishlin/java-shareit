package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

/**
 * Контракт сервиса для работы с запросами вещей.
 */
public interface ItemRequestService {
    /**
     * Создать запрос вещи.
     *
     * @param request запрос вещи.
     * @return запрос вещи.
     */
    ItemRequest createItemRequest(ItemRequest request);

    /**
     * Получить список запросов вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список запросов вещей.
     */
    Collection<ItemRequest> getUserItemRequests(long userId);

    /**
     * Получить список запросов вещей других пользователей.
     *
     * @param userId идентификатор пользователя.
     * @return список запросов вещей.
     */
    Collection<ItemRequest> getOtherUsersItemRequests(long userId);

    /**
     * Получить запрос вещи.
     *
     * @param userId    идентификатор пользователя.
     * @param requestId идентификатор запроса вещи.
     * @return запрос вещи.
     */
    ItemRequestWithItemsDto getRequest(long userId, long requestId);
}
