package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Трансферный объект для запроса добавления комментария к вещи.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public final class CreateCommentDto {
    /**
     * Содержимое комментария.
     */
    private String text;
}
