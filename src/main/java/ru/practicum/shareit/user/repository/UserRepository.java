package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

/**
 * Контракт для хранилища пользователей.
 */
public interface UserRepository {
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
    Optional<User> getUserById(long userId);

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
