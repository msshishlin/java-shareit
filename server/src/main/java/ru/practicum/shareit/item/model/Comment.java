package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Комментарий.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "comments", schema = "public")
@ToString
public final class Comment {
    /**
     * Идентификатор комментария.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Содержимое комментария.
     */
    @Column(name = "text", length = 512, nullable = false)
    private String text;

    /**
     * Вещь, к которой относится комментарий.
     */
    @JoinColumn(name = "item_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Item item;

    /**
     * Автор комментария.
     */
    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private User author;

    /**
     * Дата создания комментария.
     */
    @Column(name = "created_at")
    private LocalDateTime created;
}
