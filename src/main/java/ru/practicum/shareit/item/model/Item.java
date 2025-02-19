package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Вещь.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "items", schema = "public")
@ToString
public final class Item {
    /**
     * Идентификатор вещи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Название вещи.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Описание вещи.
     */
    @Column(name = "description", length = 512, nullable = false)
    private String description;

    /**
     * Признак доступности вещи для аренды.
     */
    @Column(name = "available", nullable = false)
    private Boolean available;

    /**
     * Владелец вещи.
     */
    @JoinColumn(name = "owner_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User owner;

    /**
     * Комментарии к вещи.
     */
    @CollectionTable(name = "comments", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "text")
    @ElementCollection
    @ToString.Exclude
    private List<String> comments;
}
