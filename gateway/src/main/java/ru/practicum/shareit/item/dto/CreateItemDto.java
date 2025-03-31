package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Трансферный объект для запроса создания вещи.
 */
@Data
public final class CreateItemDto {
    /**
     * Название вещи.
     */
    @NotBlank(message = "Название вещи не может быть пустым")
    private final String name;

    /**
     * Описание вещи.
     */
    @NotBlank(message = "Описание вещи не может быть пустым")
    private final String description;

    /**
     * Признак доступности вещи для аренды.
     */
    @NotNull(message = "Не задан признак доступности вещи для аренды")
    private final Boolean available;

    /**
     * Идентификатор запроса вещи.
     */
    private final Long requestId;
}
