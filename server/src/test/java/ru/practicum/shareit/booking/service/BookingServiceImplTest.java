package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.generator.StringGenerator;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Тесты сервиса для работы с бронями.
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class BookingServiceImplTest {
    private final EntityManager entityManager;

    /**
     * Сервис для работы с бронями.
     */
    private final BookingService bookingService;

    /**
     * Сервис для работы с вещами.
     */
    private final ItemService itemService;

    /**
     * Сервисы для работы с пользователями.
     */
    private final UserService userService;

    /**
     * Бронь.
     */
    private Booking booking;

    /**
     * Вещь.
     */
    private Item item;

    /**
     * Пользователь.
     */
    private User user;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .name(StringGenerator.generateUserName())
                .email(StringGenerator.generateUserEmail())
                .build();
        userService.createUser(user);

        item = Item.builder()
                .name(StringGenerator.generateItemName())
                .description(StringGenerator.generateItemDescription())
                .available(true)
                .owner(user)
                .build();
        itemService.createItem(item);

        booking = Booking.builder()
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(4))
                .build();
        bookingService.createBooking(booking);
    }

    @Test
    void createBookingTest() {
        TypedQuery<Booking> query = entityManager.createQuery("SELECT b FROM Booking b WHERE b.item = :item", Booking.class)
                .setParameter("item", item);

        Booking bookingFromDb = query.getSingleResult();

        MatcherAssert.assertThat(bookingFromDb.getId(), CoreMatchers.not(0));
        MatcherAssert.assertThat(bookingFromDb.getItem().getId(), Matchers.equalTo(item.getId()));
        MatcherAssert.assertThat(bookingFromDb.getBooker().getId(), Matchers.equalTo(user.getId()));
    }

    @Test
    void createBookingForUnavailableItemTest() {
        Item otherItem = Item.builder()
                .name(StringGenerator.generateItemName())
                .description(StringGenerator.generateItemDescription())
                .available(false)
                .owner(user)
                .build();
        itemService.createItem(otherItem);

        Booking otherBooking = Booking.builder()
                .item(otherItem)
                .booker(user)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(4))
                .build();

        Assertions.assertThrows(BookingException.class, () -> bookingService.createBooking(otherBooking));
    }

    @Test
    void getBookingTest() {
        Booking bookingFromDb = bookingService.getBooking(booking.getId(), user.getId());

        MatcherAssert.assertThat(bookingFromDb.getId(), Matchers.equalTo(booking.getId()));
        MatcherAssert.assertThat(bookingFromDb.getItem().getId(), Matchers.equalTo(item.getId()));
        MatcherAssert.assertThat(bookingFromDb.getItem().getName(), Matchers.equalTo(item.getName()));
        MatcherAssert.assertThat(bookingFromDb.getItem().getDescription(), Matchers.equalTo(item.getDescription()));
        MatcherAssert.assertThat(bookingFromDb.getBooker().getId(), Matchers.equalTo(user.getId()));
        MatcherAssert.assertThat(bookingFromDb.getBooker().getName(), Matchers.equalTo(user.getName()));
        MatcherAssert.assertThat(bookingFromDb.getBooker().getEmail(), Matchers.equalTo(user.getEmail()));
    }

    @Test
    void getBookingForNonExistentUserTest() {
        Assertions.assertThrows(AccessDeniedException.class, () -> bookingService.getBooking(booking.getId(), Long.MAX_VALUE));
    }

    @Test
    void getAllBookingsTest() {
        for (int i = 0; i < 4; i++) {
            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(user)
                    .status(BookingStatus.WAITING)
                    .start(LocalDateTime.now().plusDays(1))
                    .end(LocalDateTime.now().plusDays(4))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getBookings(user.getId(), BookingSearchState.ALL);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(5));
    }

    @Test
    void getPastBookingsTest() {
        for (int i = 0; i < 4; i++) {
            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(user)
                    .status(BookingStatus.WAITING)
                    .start(LocalDateTime.now().minusDays(5))
                    .end(LocalDateTime.now().minusDays(3))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getBookings(user.getId(), BookingSearchState.PAST);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(4));
    }

    @Test
    void getFutureBookingsTest() {
        for (int i = 0; i < 4; i++) {
            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(user)
                    .status(BookingStatus.WAITING)
                    .start(LocalDateTime.now().plusDays(3))
                    .end(LocalDateTime.now().plusDays(5))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getBookings(user.getId(), BookingSearchState.FUTURE);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(5));
    }

    @Test
    void getCurrentBookingsTest() {
        for (int i = 0; i < 4; i++) {
            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(user)
                    .status(BookingStatus.WAITING)
                    .start(LocalDateTime.now().minusDays(5))
                    .end(LocalDateTime.now().plusDays(5))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getBookings(user.getId(), BookingSearchState.CURRENT);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(4));
    }

    @Test
    void getWaitingBookingsTest() {
        for (int i = 0; i < 4; i++) {
            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(user)
                    .status(BookingStatus.WAITING)
                    .start(LocalDateTime.now().minusDays(5))
                    .end(LocalDateTime.now().plusDays(5))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getBookings(user.getId(), BookingSearchState.WAITING);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(5));
    }

    @Test
    void getRejectedBookingsTest() {
        for (int i = 0; i < 4; i++) {
            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(user)
                    .status(BookingStatus.REJECTED)
                    .start(LocalDateTime.now().minusDays(5))
                    .end(LocalDateTime.now().plusDays(5))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getBookings(user.getId(), BookingSearchState.REJECTED);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(4));
    }

    @Test
    void getBookingsForNonExistentUserTest() {
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getBookings(Long.MAX_VALUE, BookingSearchState.CURRENT));
    }

    @Test
    void getAllOwnerBookingsTest() {
        for (int i = 0; i < 4; i++) {
            User otherUser = User.builder()
                    .name(StringGenerator.generateUserName())
                    .email(StringGenerator.generateUserEmail())
                    .build();
            userService.createUser(otherUser);

            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(otherUser)
                    .status(BookingStatus.WAITING)
                    .start(LocalDateTime.now().plusDays(1))
                    .end(LocalDateTime.now().plusDays(4))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getOwnerBookings(user.getId(), BookingSearchState.ALL);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(5));
    }

    @Test
    void getPastOwnerBookingsTest() {
        for (int i = 0; i < 4; i++) {
            User otherUser = User.builder()
                    .name(StringGenerator.generateUserName())
                    .email(StringGenerator.generateUserEmail())
                    .build();
            userService.createUser(otherUser);

            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(otherUser)
                    .status(BookingStatus.WAITING)
                    .start(LocalDateTime.now().minusDays(5))
                    .end(LocalDateTime.now().minusDays(3))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getOwnerBookings(user.getId(), BookingSearchState.PAST);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(4));
    }

    @Test
    void getFutureOwnerBookingsTest() {
        for (int i = 0; i < 4; i++) {
            User otherUser = User.builder()
                    .name(StringGenerator.generateUserName())
                    .email(StringGenerator.generateUserEmail())
                    .build();
            userService.createUser(otherUser);

            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(otherUser)
                    .status(BookingStatus.WAITING)
                    .start(LocalDateTime.now().plusDays(3))
                    .end(LocalDateTime.now().plusDays(5))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getOwnerBookings(user.getId(), BookingSearchState.FUTURE);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(5));
    }

    @Test
    void getCurrentOwnerBookingsTest() {
        for (int i = 0; i < 4; i++) {
            User otherUser = User.builder()
                    .name(StringGenerator.generateUserName())
                    .email(StringGenerator.generateUserEmail())
                    .build();
            userService.createUser(otherUser);

            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(otherUser)
                    .status(BookingStatus.WAITING)
                    .start(LocalDateTime.now().minusDays(5))
                    .end(LocalDateTime.now().plusDays(5))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getOwnerBookings(user.getId(), BookingSearchState.CURRENT);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(0));
    }

    @Test
    void getOwnerBookingsForNonExistentUserTest() {
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getOwnerBookings(Long.MAX_VALUE, BookingSearchState.CURRENT));
    }

    @Test
    void getWaitingOwnerBookingsTest() {
        for (int i = 0; i < 4; i++) {
            User otherUser = User.builder()
                    .name(StringGenerator.generateUserName())
                    .email(StringGenerator.generateUserEmail())
                    .build();
            userService.createUser(otherUser);

            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(otherUser)
                    .status(BookingStatus.WAITING)
                    .start(LocalDateTime.now().minusDays(5))
                    .end(LocalDateTime.now().plusDays(5))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getOwnerBookings(user.getId(), BookingSearchState.WAITING);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(5));
    }

    @Test
    void getRejectedOwnerBookingsTest() {
        for (int i = 0; i < 4; i++) {
            User otherUser = User.builder()
                    .name(StringGenerator.generateUserName())
                    .email(StringGenerator.generateUserEmail())
                    .build();
            userService.createUser(otherUser);

            Booking otherBooking = Booking.builder()
                    .item(item)
                    .booker(otherUser)
                    .status(BookingStatus.REJECTED)
                    .start(LocalDateTime.now().minusDays(5))
                    .end(LocalDateTime.now().plusDays(5))
                    .build();
            bookingService.createBooking(otherBooking);
        }

        Collection<Booking> bookings = bookingService.getOwnerBookings(user.getId(), BookingSearchState.REJECTED);
        MatcherAssert.assertThat(bookings.size(), Matchers.equalTo(4));
    }

    @Test
    void approveBookingTest() {
        bookingService.approveBooking(booking.getId(), user.getId(), true);

        Booking bookingFromDb = bookingService.getBooking(booking.getId(), user.getId());
        MatcherAssert.assertThat(bookingFromDb.getStatus(), Matchers.equalTo(BookingStatus.APPROVED));
    }

    @Test
    void rejectBookingTest() {
        bookingService.approveBooking(booking.getId(), user.getId(), false);

        Booking bookingFromDb = bookingService.getBooking(booking.getId(), user.getId());
        MatcherAssert.assertThat(bookingFromDb.getStatus(), Matchers.equalTo(BookingStatus.REJECTED));
    }

    @Test
    void approveBookingByNonExistentUserTest() {
        Assertions.assertThrows(AccessDeniedException.class, () -> bookingService.approveBooking(booking.getId(), Long.MAX_VALUE, true));
    }
}
