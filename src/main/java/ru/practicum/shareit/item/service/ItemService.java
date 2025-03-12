package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

/**
 * Контракт сервиса для работы с вещами.
 */
public interface ItemService {
    /**
     * Создать вещь.
     *
     * @param item вещь.
     * @return вещь.
     */
    Item createItem(Item item);

    /**
     * Получить список вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список вещей пользователя.
     */
    Collection<ExtendedItemDto> getUserItems(long userId);

    /**
     * Получить вещь по её идентификатору.
     *
     * @param itemId идентификатор вещи.
     * @return вещь.
     */
    ExtendedItemDto getItemById(long itemId);

    /**
     * Получить вещь по её идентификатору.
     *
     * @param itemId  идентификатор вещи.
     * @param ownerId идентификатор владельца вещи.
     * @return вещь.
     */
    Item getItemById(long itemId, long ownerId);

    /**
     * Поиск вещей.
     *
     * @param text текст для поиска.
     * @return список вещей.
     */
    Collection<Item> search(String text);

    /**
     * Обновить вещь.
     *
     * @param item вещь.
     * @return вещь.
     */
    Item updateItem(Item item);

    /**
     * Добавить комментарий.
     *
     * @param comment комментарий.
     * @return комментарий.
     */
    Comment addCommentToItem(Comment comment);
}
