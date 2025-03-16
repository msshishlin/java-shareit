package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Трансферный объект для запроса добавления запроса вещи.
 */
@Data
@NoArgsConstructor
public final class CreateItemRequestDto {
    /**
     * Описание требуемой вещи
     */
    @NotBlank(message = "Описание требуемой вещи не может быть пустым")
    @Length(max = 512, message = "Описание требуемой вещи не может превышать 512 символов.")
    private String description;
}
