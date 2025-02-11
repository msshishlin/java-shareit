package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

/**
 * Маппер для моделей вещи.
 */
public final class ItemMapper {
    /**
     * Преобразовать трансферный объект для запроса создания вещи в объект вещи.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param dto     трансферный объект для запроса создания вещи.
     * @return вещь.
     */
    public static Item mapToItem(long ownerId, CreateItemDto dto) {
        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .ownerId(ownerId)
                .build();
    }

    /**
     * Преобразовать трансферный объект для запроса обновления вещи в объект вещи.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param itemId  идентификатор вещи.
     * @param dto     трансферный объект для запроса обновления вещи.
     * @return вещь.
     */
    public static Item mapToItem(long itemId, long ownerId, UpdateItemDto dto) {
        return Item.builder()
                .id(itemId)
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .ownerId(ownerId)
                .build();
    }

    /**
     * Преобразовать объект вещи в трансферный объект для сущности "Вещь".
     *
     * @param item вещь.
     * @return трансферный объект для сущности "Вещь".
     */
    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    /**
     * Преобразовать список вещей в список трансферных объектов для сущности "Вещь".
     *
     * @param itemCollection список вещей.
     * @return список трансферных объектов для сущности "Вещь".
     */
    public static Collection<ItemDto> mapToItemDtoCollection(Collection<Item> itemCollection) {
        return itemCollection.stream().map(ItemMapper::mapToItemDto).toList();
    }
}
