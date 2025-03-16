package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Трансферный объект для сущности "Комментарий".
 */
@Builder(toBuilder = true)
@Data
public class CommentDto {
    /**
     * Идентификатор комментария.
     */
    private final long id;

    /**
     * Содержимое комментария.
     */
    private final String text;

    /**
     * Вещь, к которой относится комментарий.
     */
    private final ItemDto item;

    /**
     * Автор комментария.
     */
    private final String authorName;

    /**
     * Дата создания комментария.
     */
    private final LocalDateTime created;
}
