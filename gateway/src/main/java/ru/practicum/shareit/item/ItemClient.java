package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Map;

/**
 * REST-клиент для работы с вещами.
 */
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    /**
     * Конструктор.
     *
     * @param serverUrl адрес сервера.
     * @param builder
     */
    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Создать вещь.
     *
     * @param ownerId идентификатор владельца вещи.
     * @param dto     трансферный объект для запроса создания вещи.
     * @return транспортный объект вещи.
     */
    public ResponseEntity<Object> createItem(long ownerId, CreateItemDto dto) {
        return post("", ownerId, dto);
    }

    /**
     * Получить список вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список вещей пользователя.
     */
    public ResponseEntity<Object> getItems(long userId) {
        return get("", userId);
    }

    /**
     * Получить вещь по её идентификатору.
     *
     * @param itemId  идентификатор вещи.
     * @param ownerId идентификатор владельца вещи.
     * @return вещь.
     */
    public ResponseEntity<Object> getItemById(long itemId, long ownerId) {
        return get("/" + itemId, ownerId);
    }

    /**
     * Обновить вещь.
     *
     * @param itemId  идентификатор вещи.
     * @param ownerId идентификатор владельца вещи.
     * @param dto     трансферный объект для запроса обновления вещи.
     * @return транспортный объект вещи.
     */
    public ResponseEntity<Object> updateItem(long itemId, long ownerId, UpdateItemDto dto) {
        return patch("/" + itemId, ownerId, dto);
    }

    /**
     * Поиск вещей.
     *
     * @param userId идентификатор пользователя.
     * @param text   текст для поиска.
     * @return список вещей.
     */
    public ResponseEntity<Object> search(long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );

        return get("?text={text}", userId, parameters);
    }

    /**
     * Добавить комментарий.
     *
     * @param authorId идентификатор автора.
     * @param itemId   идентификатор вещи.
     * @param dto      трансферный объект для запроса добавления комментария к вещи.
     * @return комментарий
     */
    public ResponseEntity<Object> addCommentToItem(long authorId, long itemId, CreateCommentDto dto) {
        return post("/" + itemId + "/comment", authorId, dto);
    }
}
