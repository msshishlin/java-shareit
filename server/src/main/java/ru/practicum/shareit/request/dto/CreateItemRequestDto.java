package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Трансферный объект для запроса добавления запроса вещи.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
public final class CreateItemRequestDto {
    /**
     * Описание требуемой вещи
     */
    private String description;
}
