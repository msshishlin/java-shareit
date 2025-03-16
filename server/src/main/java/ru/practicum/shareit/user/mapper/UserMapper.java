package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * Маппер для моделей пользователя.
 */
public final class UserMapper {
    /**
     * Преобразовать трансферный объект для запроса создания пользователя в объект пользователя.
     *
     * @param dto трансферный объект для запроса создания пользователя
     * @return пользователь.
     */
    public static User mapToUser(CreateUserDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    /**
     * Преобразовать трансферный объект для запроса обновления пользователя в объект пользователя.
     *
     * @param dto трансферный объект для запроса обновления пользователя
     * @return пользователь.
     */
    public static User mapToUser(long userId, UpdateUserDto dto) {
        return User.builder()
                .id(userId)
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    /**
     * Преобразовать объект пользователя в трансферный объект для сущности "Пользователь".
     *
     * @param user пользователь.
     * @return трансферный объект для сущности "Пользователь".
     */
    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
