package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * Контроллер для запросов к вещам.
 */
@RequiredArgsConstructor
@RequestMapping("/items")
@RestController
public final class ItemController {
    /**
     * Сервис для работы с вещами.
     */
    private final ItemService itemService;

    /**
     * Создать вещь.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param dto     трансферный объект для запроса создания вещи.
     * @return транспортный объект вещи.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader(name = "X-Sharer-User-Id") long ownerId, @RequestBody @Valid CreateItemDto dto) {
        return ItemMapper.mapToItemDto(itemService.createItem(ItemMapper.mapToItem(ownerId, dto)));
    }

    /**
     * Получить список вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список вещей пользователя.
     */
    @GetMapping
    public Collection<ExtendedItemDto> getUserItems(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return itemService.getUserItems(userId);
    }

    /**
     * Получить вещь по её идентификатору.
     *
     * @param itemId  идентификатор вещи.
     * @param ownerId идентификатор владельца вещи.
     * @return вещь.
     */
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId, @RequestHeader(name = "X-Sharer-User-Id") long ownerId) {
        return ItemMapper.mapToItemDto(itemService.getItemById(itemId, ownerId));
    }

    /**
     * Поиск вещей.
     *
     * @param text текст для поиска.
     * @return список вещей.
     */
    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(required = false) String text) {
        return ItemMapper.mapToItemDtoCollection(itemService.search(text));
    }

    /**
     * Обновить вещь.
     *
     * @param itemId  идентификатор вещи.
     * @param ownerId идентификатор владельца вещи.
     * @param dto     трансферный объект для запроса обновления вещи.
     * @return транспортный объект вещи.
     */
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId, @RequestHeader(name = "X-Sharer-User-Id") long ownerId, @RequestBody @Valid UpdateItemDto dto) {
        return ItemMapper.mapToItemDto(itemService.updateItem(ItemMapper.mapToItem(itemId, ownerId, dto)));
    }
}
