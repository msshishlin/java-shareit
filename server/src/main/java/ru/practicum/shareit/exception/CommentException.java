package ru.practicum.shareit.exception;

/**
 * Исключение, выбрасываемое при невозможности добавления комментария.
 */
public class CommentException extends RuntimeException {
    /**
     * Конструктор.
     *
     * @param message сообщение.
     */
    public CommentException(String message) {
        super(message);
    }
}
