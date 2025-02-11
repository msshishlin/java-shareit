package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

/**
 * Контракт сервиса для работы с пользователями.
 */
public interface UserService {
    /**
     * Создать пользователя.
     *
     * @param user пользователь.
     * @return пользователь.
     */
    User createUser(User user);

    /**
     * Получить пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return пользователь.
     */
    User getUserById(long userId);

    /**
     * Обновить пользователя.
     *
     * @param user пользователь.
     * @return пользователь.
     */
    User updateUser(User user);

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    void deleteUser(long userId);
}
