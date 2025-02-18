package ru.practicum.shareit.exception;

/**
 * Исключение, выбрасываемое при невозможности создания брони.
 */
public class BookingException extends RuntimeException {
    /**
     * Конструктор.
     *
     * @param message сообщение.
     */
    public BookingException(String message) {
        super(message);
    }
}
