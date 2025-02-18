package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;

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
                .owner(User.builder().id(ownerId).build())
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
                .owner(User.builder().id(ownerId).build())
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
     * Преобразовать объект вещи в расширенный трансферный объект для сущности "Вещь".
     *
     * @param item вещь.
     * @return расширенный трансферный объект для сущности "Вещь".
     */
    public static ExtendedItemDto mapToExtendedItemDto(Item item, Collection<Booking> itemBookings) {
        ExtendedItemDto.ExtendedItemDtoBuilder builder = ExtendedItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable());

        itemBookings.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()))
                .findFirst()
                .ifPresent(booking -> builder.lastBooking(booking.getEnd()));

        itemBookings.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .ifPresent(booking -> builder.nextBooking(booking.getStart()));

        return builder.build();
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

    /**
     * Преобразовать список вещей в список расширенных трансферных объектов для сущности "Вещь".
     *
     * @param itemCollection список вещей.
     * @return список расширенных трансферных объектов для сущности "Вещь".
     */
    public static Collection<ExtendedItemDto> mapToExtendedItemDtoCollection(Collection<Item> itemCollection, Collection<Booking> itemBookingsCollection) {
        return itemCollection.stream().map(i -> mapToExtendedItemDto(i, itemBookingsCollection.stream().filter(b -> b.getItem().getId() == i.getId()).toList())).toList();
    }
}
