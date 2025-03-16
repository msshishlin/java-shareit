package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Трансферный объект для запроса добавления запроса вещи.
 */
@Data
@NoArgsConstructor
public final class CreateItemRequestDto {
    /**
     * Описание требуемой вещи
     */
    private String description;
}
