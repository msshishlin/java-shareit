package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Хранилище вещей в оперативной памяти.
 */
@Repository
@RequiredArgsConstructor
public final class InMemoryItemRepository implements ItemRepository {
    /**
     * Список вещей.
     */
    private final Map<Long, Item> items;

    /**
     * Создать вещь.
     *
     * @param item вещь.
     * @return вещь.
     */
    @Override
    public Item createItem(Item item) {
        item.setId(getNextId());

        items.put(item.getId(), item);
        return item;
    }

    /**
     * Получить список вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список вещей пользователя.
     */
    @Override
    public Collection<Item> getUserItems(long userId) {
        return items.values().stream().filter(i -> i.getOwnerId() == userId).toList();
    }

    /**
     * Получить вещь по её идентификатору.
     *
     * @param itemId идентификатор вещи.
     * @return вещь.
     */
    @Override
    public Optional<Item> getItemById(long itemId) {
        if (items.containsKey(itemId)) {
            return Optional.of(items.get(itemId));
        }

        return Optional.empty();
    }

    /**
     * Поиск вещей.
     *
     * @param text текст для поиска.
     * @return список вещей.
     */
    @Override
    public Collection<Item> search(String text) {
        String lowerText = text.toLowerCase();

        return items.values().stream().filter(i -> i.getAvailable() && (i.getName().toLowerCase().contains(lowerText) || i.getDescription().toLowerCase().contains(lowerText))).toList();
    }

    /**
     * Обновить вещь.
     *
     * @param item вещь.
     * @return вещь.
     */
    @Override
    public Item updateItem(Item item) {
        Item oldItem = items.get(item.getId());

        if (oldItem.getOwnerId() != item.getOwnerId()) {
            throw new AccessDeniedException(String.format("Доступ к редактированию вещи с id = %d запрещен", item.getId()));
        }

        if (item.getName() != null && !item.getName().isBlank()) {
            oldItem.setName(item.getName());
        }

        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }

        return oldItem;
    }

    // region Facilities

    /**
     * Получить идентификатор для создания новой вещи.
     *
     * @return идентификатор для создания новой вещи.
     */
    private long getNextId() {
        long currentMaxId = this.items.values()
                .stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }

    // endregion
}
