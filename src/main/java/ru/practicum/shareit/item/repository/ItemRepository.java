package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

/**
 * Контракт для хранилища вещей.
 */
public interface ItemRepository {
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
    Collection<Item> getUserItems(long userId);

    /**
     * Получить вещь по её идентификатору.
     *
     * @param itemId идентификатор вещи.
     * @return вещь.
     */
    Optional<Item> getItemById(long itemId);

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
}
