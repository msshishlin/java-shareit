package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Пользователь.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "users", schema = "public")
@ToString
public final class User {
    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Имя пользователя.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Адрес электронной почты пользователя.
     */
    @Column(name = "email", length = 512, nullable = false, unique = true)
    private String email;
}
