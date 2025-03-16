package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

/**
 * Сервис для работы с пользователями.
 */
@RequiredArgsConstructor
@Service
public final class UserServiceImpl implements UserService {
    /**
     * Хранилище пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Создать пользователя.
     *
     * @param user пользователь.
     * @return пользователь.
     */
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Получить пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return пользователь.
     */
    @Override
    public User getUserById(long userId) {
        Optional<User> userOptional =  userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }

        return userOptional.get();
    }

    /**
     * Обновить пользователя.
     *
     * @param user пользователь.
     * @return пользователь.
     */
    @Override
    public User updateUser(User user) {
        User oldUser = getUserById(user.getId());

        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            oldUser.setEmail(user.getEmail());
        }

        return userRepository.save(oldUser);
    }

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    @Override
    public void deleteUser(long userId) {
        throwIfUserNotFound(userId);
        userRepository.deleteById(userId);
    }

    //region Facilities

    /**
     * Выбросить исключение, если пользователей не найден.
     *
     * @param userId идентификатор пользователя.
     */
    private void throwIfUserNotFound(long userId) {
        if (this.userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }
    }

    //endregion
}
