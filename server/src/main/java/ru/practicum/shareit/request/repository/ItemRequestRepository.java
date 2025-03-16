package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Контракт хранилища запросов вещей.
 */
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    /**
     * Получить список запросов вещей по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список запросов вещей.
     */
    List<ItemRequest> findByRequesterId(long userId);

    /**
     * Получить список запросов вещей других пользователей, отсортированных от более новых к более старым.
     *
     * @param userId идентификатор пользователя.
     * @return список запросов вещей.
     */
    List<ItemRequest> findByRequesterIdNotOrderByCreatedDesc(long userId);
}
