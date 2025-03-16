package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Маппер для запросов вещи.
 */
public final class ItemRequestMapper {
    /**
     * Преобразовать трансферный объект для создания запроса вещи в объект запроса вещи.
     *
     * @param requesterId идентификатор пользователя, создавшего запрос.
     * @param dto         трансферный объект для создания запроса вещи.
     * @return запрос вещи.
     */
    public static ItemRequest mapToItemRequest(long requesterId, CreateItemRequestDto dto) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .requester(User.builder().id(requesterId).build())
                .created(LocalDateTime.now())
                .build();
    }

    /**
     * Преобразовать объект запроса вещи в трансферный объект для сущности "Запрос вещи".
     *
     * @param request запрос вещи.
     * @return трансферный объект для сущности "Запрос вещи".
     */
    public static ItemRequestDto mapToItemRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }

    /**
     * Преобразовать объект запроса вещи в трансферный объект для сущности "Запрос вещи".
     *
     * @param request запрос вещи.
     * @param items коллекция вещей по запросу.
     * @return трансферный объект для сущности "Запрос вещи".
     */
    public static ItemRequestWithItemsDto mapToItemRequestWithItemsDto(ItemRequest request, Collection<Item> items) {
        return ItemRequestWithItemsDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(items)
                .build();
    }

    /**
     * Преобразовать список запросов вещей в список трансферных объектов для сущности "Запрос вещи".
     *
     * @param itemRequestCollection список запросов вещей.
     * @return список трансферных объектов для сущности "Запрос вещи".
     */
    public static Collection<ItemRequestDto> mapToItemRequestDtoCollection(Collection<ItemRequest> itemRequestCollection) {
        return itemRequestCollection.stream().map(ItemRequestMapper::mapToItemRequestDto).toList();
    }
}
