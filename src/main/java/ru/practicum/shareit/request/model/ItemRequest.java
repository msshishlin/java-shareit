package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Запрос вещи.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "item_requests", schema = "public")
@ToString
public final class ItemRequest {
    /**
     * Идентификатор запроса.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Описание требуемой вещи.
     */
    @Column(name = "description", length = 512, nullable = false)
    private String description;

    /**
     * Пользователь, создавший запрос.
     */
    @JoinColumn(name = "requester_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User requester;

    /**
     * Дата и время создания запроса.
     */
    @Column(name = "created_at")
    private LocalDateTime created;
}
