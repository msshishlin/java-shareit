package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Сервис для работы с вещами.
 */
@RequiredArgsConstructor
@Service
public final class ItemServiceImpl implements ItemService {
    /**
     * Хранилище вещей.
     */
    private final ItemRepository itemRepository;

    /**
     * Хранилище пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Создать вещь.
     *
     * @param item вещь.
     * @return вещь.
     */
    @Override
    public Item createItem(Item item) {
        throwIfUserNotFound(item.getOwnerId());
        return itemRepository.createItem(item);
    }

    /**
     * Получить список вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список вещей пользователя.
     */
    @Override
    public Collection<Item> getUserItems(long userId) {
        throwIfUserNotFound(userId);
        return itemRepository.getUserItems(userId);
    }

    /**
     * Получить вещь по её идентификатору.
     *
     * @param itemId  идентификатор вещи.
     * @param ownerId идентификатор владельца вещи.
     * @return вещь.
     */
    @Override
    public Item getItemById(long itemId, long ownerId) {
        Optional<Item> itemOptional = itemRepository.getItemById(itemId);

        if (itemOptional.isEmpty()) {
            throw new NotFoundException(String.format("Вещь с id = %d не найдена", itemId));
        }

        return itemOptional.get();
    }

    /**
     * Поиск вещей.
     *
     * @param text текст для поиска.
     * @return список вещей.
     */
    @Override
    public Collection<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text);
    }

    /**
     * Обновить вещь.
     *
     * @param item вещь.
     * @return вещь.
     */
    @Override
    public Item updateItem(Item item) {
        throwIfItemNotFound(item.getId());
        throwIfUserNotFound(item.getOwnerId());

        return itemRepository.updateItem(item);
    }

    //region Facilities

    /**
     * Выбросить исключение, если вещь не найдена.
     *
     * @param itemId идентификатор вещи.
     */
    private void throwIfItemNotFound(long itemId) {
        if (this.itemRepository.getItemById(itemId).isEmpty()) {
            throw new NotFoundException(String.format("Вещь с id = %d не найдена", itemId));
        }
    }

    /**
     * Выбросить исключение, если пользователей не найден.
     *
     * @param userId идентификатор пользователя.
     */
    private void throwIfUserNotFound(long userId) {
        if (this.userRepository.getUserById(userId).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }
    }

    //endregion
}
