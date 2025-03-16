package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

/**
 * REST-клиент для работы с пользователями.
 */
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    /**
     * Конструктор.
     *
     * @param serverUrl адрес сервера.
     * @param builder
     */
    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Создать пользователя.
     *
     * @param dto трансферный объект для запроса создания пользователя.
     * @return трансферный объект пользователя.
     */
    public ResponseEntity<Object> createUser(CreateUserDto dto) {
        return post("", dto);
    }

    /**
     * Получить пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return трансферный объект пользователя.
     */
    public ResponseEntity<Object> getUserById(long userId) {
        return get("/" + userId);
    }

    /**
     * Обновить пользователя.
     *
     * @param userId идентификатор пользователя.
     * @param dto    трансферный объект для запроса обновления пользователя.
     * @return трансферный объект пользователя.
     */
    public ResponseEntity<Object> updateUser(long userId, UpdateUserDto dto) {
        return patch("/" + userId, dto);
    }

    /**
     * Удалить пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    public void deleteUser(long userId) {
        delete("/" + userId);
    }
}
