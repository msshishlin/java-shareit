package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Трансферный объект для сущности "Запрос вещи".
 */
@Builder(toBuilder = true)
@Data
public class ItemRequestWithItemsDto {
    /**
     * Идентификатор запроса.
     */
    private final long id;

    /**
     * Описание требуемой вещи.
     */
    private final String description;

    /**
     * Дата и время создания запроса.
     */
    private final LocalDateTime created;

    /**
     * Список вещей по запросу.
     */
    private final Collection<Item> items;
}
