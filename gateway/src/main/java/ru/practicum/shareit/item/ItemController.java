package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

/**
 * Контроллер для запросов к вещам.
 */
@RequestMapping("/items")
@RequiredArgsConstructor
@RestController
public final class ItemController {
    /**
     * REST-клиент для работы с вещами.
     */
    private final ItemClient itemClient;

    /**
     * Создать вещь.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param dto     трансферный объект для запроса создания вещи.
     * @return транспортный объект вещи.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@RequestHeader(name = "X-Sharer-User-Id") long ownerId,
                                             @RequestBody @Valid CreateItemDto dto) {
        return itemClient.createItem(ownerId, dto);
    }

    /**
     * Получить список вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список вещей пользователя.
     */
    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return itemClient.getItems(userId);
    }

    /**
     * Получить вещь по её идентификатору.
     *
     * @param itemId  идентификатор вещи.
     * @param ownerId идентификатор владельца вещи.
     * @return вещь.
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId,
                                              @RequestHeader(name = "X-Sharer-User-Id") long ownerId) {
        return itemClient.getItemById(itemId, ownerId);
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
    public ResponseEntity<Object> updateItem(@PathVariable long itemId,
                                             @RequestHeader(name = "X-Sharer-User-Id") long ownerId,
                                             @RequestBody @Valid UpdateItemDto dto) {
        return itemClient.updateItem(itemId, ownerId, dto);
    }

    /**
     * Поиск вещей.
     *
     * @param text текст для поиска.
     * @return список вещей.
     */
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                         @RequestParam(required = false) String text) {
        return itemClient.search(userId, text);
    }

    /**
     * Добавить комментарий.
     *
     * @param itemId   идентификатор вещи.
     * @param authorId идентификатор автора.
     * @param dto      трансферный объект для запроса добавления комментария к вещи.
     * @return комментарий
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(@PathVariable long itemId,
                                                   @RequestHeader(name = "X-Sharer-User-Id") long authorId,
                                                   @RequestBody @Valid CreateCommentDto dto) {
        return itemClient.addCommentToItem(authorId, itemId, dto);
    }
}