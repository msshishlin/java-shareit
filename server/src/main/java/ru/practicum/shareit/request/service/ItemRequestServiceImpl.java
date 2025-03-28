package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

/**
 * Сервис для работы с запросами вещей.
 */
@RequiredArgsConstructor
@Service
public final class ItemRequestServiceImpl implements ItemRequestService {
    /**
     * Хранилище вещей.
     */
    private final ItemRepository itemRepository;

    /**
     * Хранилище запросов вещей.
     */
    private final ItemRequestRepository requestRepository;

    /**
     * Хранилище пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Создать запрос вещи.
     *
     * @param request запрос вещи.
     * @return запрос вещи.
     */
    @Override
    public ItemRequest createItemRequest(ItemRequest request) {
        throwIfUserNotFound(request.getRequester().getId());
        return requestRepository.save(request);
    }

    /**
     * Получить список запросов вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список запросов вещей.
     */
    @Override
    public Collection<ItemRequest> getUserItemRequests(long userId) {
        throwIfUserNotFound(userId);
        return requestRepository.findByRequesterId(userId);
    }

    /**
     * Получить список запросов вещей других пользователей.
     *
     * @param userId идентификатор пользователя.
     * @return список запросов вещей.
     */
    @Override
    public Collection<ItemRequest> getOtherUsersItemRequests(long userId) {
        throwIfUserNotFound(userId);
        return requestRepository.findByRequesterIdNotOrderByCreatedDesc(userId);
    }

    /**
     * Получить запрос вещи.
     *
     * @param userId    идентификатор пользователя.
     * @param requestId идентификатор запроса вещи.
     * @return запрос вещи.
     */
    @Override
    public ItemRequestWithItemsDto getRequest(long userId, long requestId) {
        throwIfUserNotFound(userId);
        return ItemRequestMapper.mapToItemRequestWithItemsDto(
                requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(String.format("Запрос вещи с id = %d не найден", requestId))),
                itemRepository.findByRequestId(requestId)
        );
    }

    //region Facilities

    /**
     * Выбросить исключение, если пользователей не найден.
     *
     * @param userId идентификатор пользователя.
     */
    private void throwIfUserNotFound(long userId) {
        if (!this.userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }
    }

    //endregion
}
