package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;

/**
 * REST-клиент для работы с запросами вещей.
 */
@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    /**
     * Конструктор.
     * @param serverUrl адрес сервера.
     * @param builder
     */
    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Создать запрос вещи.
     *
     * @param requesterId идентификатор пользователя, создавшего запрос.
     * @param dto         трансферный объект для создания запроса вещи.
     */
    public ResponseEntity<Object> createRequest(long requesterId, CreateItemRequestDto dto) {
        return post("", requesterId, dto);
    }

    /**
     * Получить список запросов вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     */
    public ResponseEntity<Object> getUserRequests(long userId) {
        return get("", userId);
    }

    /**
     * Получить список запросов вещей других пользователей.
     *
     * @param userId идентификатор пользователя.
     */
    public ResponseEntity<Object> getOtherUsersRequests(long userId) {
        return get("/all", userId);
    }

    /**
     * Получить запрос вещи по идентификатору.
     *
     * @param userId    идентификатор пользователя.
     * @param requestId идентификатор запроса вещи.
     * @return запрос вещи.
     */
    public ResponseEntity<Object> getRequest(long userId, long requestId) {
        return get("/" + requestId, userId);
    }
}
