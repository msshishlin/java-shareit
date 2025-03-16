package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
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
     * Хранилище комментариев.
     */
    private final CommentRepository commentRepository;

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
    public ExtendedItemDto getItemById(long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найдена", itemId)));

        Collection<Booking> bookings = bookingRepository.findByItemId(itemId);

        return ItemMapper.mapToExtendedItemDto(item, bookings);
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

    /**
     * Добавить комментарий.
     *
     * @param comment комментарий.
     * @return комментарий.
     */
    @Override
    public Comment addCommentToItem(Comment comment) {
        Collection<Booking> authorBookings = bookingRepository.findByBookerIdAndItemId(comment.getAuthor().getId(), comment.getItem().getId());

        if (authorBookings.isEmpty() || authorBookings.stream().filter(b -> b.getEnd().isBefore(LocalDateTime.now())).toList().isEmpty()) {
            throw new CommentException(String.format("Пользователь с id = %d не может оставить комментарии к вещи с id = %d", comment.getAuthor().getId(), comment.getItem().getId()));
        }

        comment.setItem(itemRepository.findById(comment.getItem().getId()).orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найдена", comment.getItem().getId()))));
        comment.setAuthor(userRepository.findById(comment.getAuthor().getId()).orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", comment.getAuthor().getId()))));

        return commentRepository.save(comment);
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
