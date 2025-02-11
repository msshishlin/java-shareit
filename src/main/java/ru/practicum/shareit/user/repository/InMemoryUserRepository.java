package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * Хранилище пользователей в оперативной памяти.
 */
@Repository
@RequiredArgsConstructor
public final class InMemoryUserRepository implements UserRepository {
    /**
     * Список пользователей.
     */
    private final HashMap<Long, User> users;

    /**
     * Создать пользователя.
     *
     * @param user пользователь.
     * @return пользователь.
     */
    @Override
    public User createUser(User user) {
        if (this.users.values().stream().anyMatch(u -> !Objects.equals(u.getId(), user.getId()) && Objects.equals(u.getEmail(), user.getEmail()))) {
            throw new DuplicateEmailException(String.format("E-mail '%s' уже используется", user.getEmail()));
        }

        user.setId(getNextId());

        users.put(user.getId(), user);
        return user;
    }

    /**
     * Получить пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return пользователь.
     */
    @Override
    public Optional<User> getUserById(long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        }

        return Optional.empty();
    }

    /**
     * Обновить пользователя.
     *
     * @param user пользователь.
     * @return пользователь.
     */
    @Override
    public User updateUser(User user) {
        if (this.users.values().stream().anyMatch(u -> !Objects.equals(u.getId(), user.getId()) && Objects.equals(u.getEmail(), user.getEmail()))) {
            throw new DuplicateEmailException(String.format("E-mail '%s' уже используется", user.getEmail()));
        }

        User oldUser = users.get(user.getId());

        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            oldUser.setEmail(user.getEmail());
        }

        return oldUser;
    }

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    // region Facilities

    /**
     * Получить идентификатор для создания нового пользователя.
     *
     * @return идентификатор для создания нового пользователя.
     */
    private long getNextId() {
        long currentMaxId = this.users.values()
                .stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }

    // endregion
}
