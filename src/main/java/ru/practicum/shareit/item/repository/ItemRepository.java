package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Контракт для хранилища вещей.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Получить список вещей пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список вещей.
     */
    List<Item> findByOwnerId(long userId);

    /**
     * Поиск доступных вещей по имени или описанию.
     *
     * @param searchText текст для поиска.
     * @return список вещей.
     */
    @Query("""
           SELECT
                    i
           FROM
                    Item i
           WHERE
                    i.available = true
                AND
                    (
                        i.name ILIKE %?1%
                     OR
                        i.description ILIKE %?1%
                    )
           """)
    List<Item> findByNameOrDescriptionContainingIgnoreCase(String searchText);
}
