package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Трансферный объект для запроса добавления комментария к вещи.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class CreateCommentDto {
    /**
     * Содержимое комментария.
     */
    @NotBlank(message = "Содержимое комментария не может быть пустым")
    @Length(max = 512, message = "Содержимое комментария не может превышать 512 символов.")
    private String text;
}
