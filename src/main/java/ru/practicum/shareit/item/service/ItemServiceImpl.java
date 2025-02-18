package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

/**
 * Сервис для работы с вещами.
 */
@RequiredArgsConstructor
@Service
public final class ItemServiceImpl implements ItemService {
    /**
     * Хранилище броней.
     */
    private final BookingRepository bookingRepository;

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
        throwIfUserNotFound(item.getOwner().getId());
        return itemRepository.save(item);
    }

    /**
     * Получить список вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список вещей пользователя.
     */
    @Override
    public Collection<ExtendedItemDto> getUserItems(long userId) {
        throwIfUserNotFound(userId);

        Collection<Item> items = itemRepository.findByOwnerId(userId);
        Collection<Booking> bookings = bookingRepository.findByItemIn(items);

        return ItemMapper.mapToExtendedItemDtoCollection(items, bookings);
    }

    /**
     * Получить вещь по её идентификатору.
     *
     * @param itemId идентификатор вещи.
     * @return вещь.
     */
    @Override
    public Item getItemById(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найдена", itemId)));
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
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найдена", itemId)));

        if (item.getOwner().getId() != ownerId) {
            throw new AccessDeniedException(String.format("Доступ к вещи с id = %d запрещен", itemId));
        }

        return item;
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

        return itemRepository.findByNameOrDescriptionContainingIgnoreCase(text);
    }

    /**
     * Обновить вещь.
     *
     * @param item вещь.
     * @return вещь.
     */
    @Override
    public Item updateItem(Item item) {
        Item oldItem = getItemById(item.getId(), item.getOwner().getId());

        if (item.getName() != null && !item.getName().isBlank()) {
            oldItem.setName(item.getName());
        }

        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }

        return itemRepository.save(oldItem);
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
