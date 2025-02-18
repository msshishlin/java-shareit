package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Бронь.
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "bookings", schema = "public")
@ToString
public final class Booking {
    /**
     * Идентификатор брони.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Дата начала бронирования.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    /**
     * Дата окончания бронирования.
     */
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    /**
     * Статус брони.
     */
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    /**
     * Пользователь, осуществивший бронь.
     */
    @JoinColumn(name = "booker_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User booker;

    /**
     * Забронированная вещь.
     */
    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Item item;
}
