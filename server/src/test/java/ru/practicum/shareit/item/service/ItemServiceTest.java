package ru.practicum.shareit.item.service;

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
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.generator.StringGenerator;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Тесты сервиса для работы с вещами.
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class ItemServiceTest {
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
     * Вещь.
     */
    private Item item;

    /**
     * Пользователь.
     */
    private User user;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        user = User.builder()
                .name(StringGenerator.generateUserName())
                .email(StringGenerator.generateUserEmail())
                .build();
        userService.createUser(user);

        if (testInfo.getDisplayName().equals("searchByNameTest") || testInfo.getDisplayName().equals("searchByDescriptionTest")) {
            return;
        }

        item = Item.builder()
                .name(StringGenerator.generateItemName())
                .description(StringGenerator.generateItemDescription())
                .available(true)
                .owner(user)
                .build();
        itemService.createItem(item);
    }

    @Test
    void createItemTest() {
        TypedQuery<Item> query = entityManager.createQuery("SELECT i FROM Item i WHERE i.name = :name", Item.class)
                .setParameter("name", item.getName());

        Item itemFromDb = query.getSingleResult();

        MatcherAssert.assertThat(itemFromDb.getId(), CoreMatchers.not(0));
        MatcherAssert.assertThat(itemFromDb.getName(), Matchers.equalTo(item.getName()));
        MatcherAssert.assertThat(itemFromDb.getDescription(), Matchers.equalTo(item.getDescription()));
        MatcherAssert.assertThat(itemFromDb.getAvailable(), Matchers.equalTo(item.getAvailable()));
        MatcherAssert.assertThat(itemFromDb.getOwner().getId(), Matchers.equalTo(user.getId()));
    }

    @Test
    void getUserItemsTest() {
        for (int i = 0; i < 4; i++) {
            Item otherItem = Item.builder()
                    .name(StringGenerator.generateItemName())
                    .description(StringGenerator.generateItemDescription())
                    .available(true)
                    .owner(user)
                    .build();

            itemService.createItem(otherItem);
        }

        Collection<ExtendedItemDto> userItems = itemService.getUserItems(user.getId());
        MatcherAssert.assertThat(userItems.size(), Matchers.equalTo(5));
    }

    @Test
    void getNonExistentUserItemsTest() {
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getUserItems(Long.MAX_VALUE));
    }

    @Test
    void getItemByIdTest() {
        ExtendedItemDto itemFromDb = itemService.getItemById(item.getId());

        MatcherAssert.assertThat(itemFromDb.getId(), Matchers.equalTo(item.getId()));
        MatcherAssert.assertThat(itemFromDb.getName(), Matchers.equalTo(item.getName()));
        MatcherAssert.assertThat(itemFromDb.getDescription(), Matchers.equalTo(item.getDescription()));
        MatcherAssert.assertThat(itemFromDb.getAvailable(), Matchers.equalTo(item.getAvailable()));
    }

    @Test
    void getItemByIdAndUserIdTest() {
        Item itemFromDb = itemService.getItemById(item.getId(), user.getId());

        MatcherAssert.assertThat(itemFromDb.getId(), Matchers.equalTo(item.getId()));
        MatcherAssert.assertThat(itemFromDb.getName(), Matchers.equalTo(item.getName()));
        MatcherAssert.assertThat(itemFromDb.getDescription(), Matchers.equalTo(item.getDescription()));
        MatcherAssert.assertThat(itemFromDb.getAvailable(), Matchers.equalTo(item.getAvailable()));
        MatcherAssert.assertThat(itemFromDb.getOwner().getId(), Matchers.equalTo(user.getId()));
        MatcherAssert.assertThat(itemFromDb.getOwner().getName(), Matchers.equalTo(user.getName()));
        MatcherAssert.assertThat(itemFromDb.getOwner().getEmail(), Matchers.equalTo(user.getEmail()));
    }

    @Test
    void getItemByIdAndNonExistentUserIdTest() {
        User otherUser = User.builder()
                .name(StringGenerator.generateUserName())
                .email(StringGenerator.generateUserEmail())
                .build();
        userService.createUser(otherUser);

        Assertions.assertThrows(AccessDeniedException.class, () -> itemService.getItemById(item.getId(), otherUser.getId()));
    }

    @Test
    void searchByNameTest() {
        for (int i = 0; i < 5; i++) {
            Item otherItem = Item.builder()
                    .name((i % 2) == 0 ? "ABCDEF" : "GHIJKL")
                    .description(StringGenerator.generateItemDescription())
                    .available(true)
                    .owner(user)
                    .build();

            itemService.createItem(otherItem);
        }

        Collection<Item> items = itemService.search("ABCDEF");
        MatcherAssert.assertThat(items.size(), Matchers.equalTo(3));
    }

    @Test
    void searchByDescriptionTest() {
        for (int i = 0; i < 5; i++) {
            Item otherItem = Item.builder()
                    .name(StringGenerator.generateItemName())
                    .description((i % 2) == 0 ? "ABCDEF" : "GHIJKL")
                    .available(true)
                    .owner(user)
                    .build();

            itemService.createItem(otherItem);
        }

        Collection<Item> items = itemService.search("ABCDEF");
        MatcherAssert.assertThat(items.size(), Matchers.equalTo(3));
    }

    @Test
    void searchByNullTest() {
        Collection<Item> items = itemService.search(null);
        MatcherAssert.assertThat(items.size(), Matchers.equalTo(0));
    }

    @Test
    void searchByBlankTextTest() {
        Collection<Item> items = itemService.search("   ");
        MatcherAssert.assertThat(items.size(), Matchers.equalTo(0));
    }

    @Test
    void updateItemNameTest() {
        Item updatedItem = Item.builder()
                .id(item.getId())
                .name(StringGenerator.generateItemName())
                .owner(item.getOwner())
                .build();
        itemService.updateItem(updatedItem);

        Item itemFromDb = itemService.getItemById(item.getId(), user.getId());

        MatcherAssert.assertThat(itemFromDb.getId(), Matchers.equalTo(item.getId()));
        MatcherAssert.assertThat(itemFromDb.getName(), Matchers.equalTo(updatedItem.getName()));
        MatcherAssert.assertThat(itemFromDb.getDescription(), Matchers.equalTo(item.getDescription()));
        MatcherAssert.assertThat(itemFromDb.getAvailable(), Matchers.equalTo(item.getAvailable()));
        MatcherAssert.assertThat(itemFromDb.getOwner().getId(), Matchers.equalTo(item.getOwner().getId()));
    }

    @Test
    void updateItemNameToBlankStringTest() {
        Item updatedItem = Item.builder()
                .id(item.getId())
                .name("   ")
                .owner(item.getOwner())
                .build();
        itemService.updateItem(updatedItem);

        Item itemFromDb = itemService.getItemById(item.getId(), user.getId());

        MatcherAssert.assertThat(itemFromDb.getId(), Matchers.equalTo(item.getId()));
        MatcherAssert.assertThat(itemFromDb.getName(), Matchers.equalTo(item.getName()));
        MatcherAssert.assertThat(itemFromDb.getDescription(), Matchers.equalTo(item.getDescription()));
        MatcherAssert.assertThat(itemFromDb.getAvailable(), Matchers.equalTo(item.getAvailable()));
        MatcherAssert.assertThat(itemFromDb.getOwner().getId(), Matchers.equalTo(item.getOwner().getId()));
    }

    @Test
    void updateItemDescriptionTest() {
        Item updatedItem = Item.builder()
                .id(item.getId())
                .description(StringGenerator.generateItemDescription())
                .owner(item.getOwner())
                .build();
        itemService.updateItem(updatedItem);

        Item itemFromDb = itemService.getItemById(item.getId(), user.getId());

        MatcherAssert.assertThat(itemFromDb.getId(), Matchers.equalTo(item.getId()));
        MatcherAssert.assertThat(itemFromDb.getName(), Matchers.equalTo(item.getName()));
        MatcherAssert.assertThat(itemFromDb.getDescription(), Matchers.equalTo(updatedItem.getDescription()));
        MatcherAssert.assertThat(itemFromDb.getAvailable(), Matchers.equalTo(item.getAvailable()));
        MatcherAssert.assertThat(itemFromDb.getOwner().getId(), Matchers.equalTo(item.getOwner().getId()));
    }

    @Test
    void updateItemDescriptionToBlankStringTest() {
        Item updatedItem = Item.builder()
                .id(item.getId())
                .description("   ")
                .owner(item.getOwner())
                .build();
        itemService.updateItem(updatedItem);

        Item itemFromDb = itemService.getItemById(item.getId(), user.getId());

        MatcherAssert.assertThat(itemFromDb.getId(), Matchers.equalTo(item.getId()));
        MatcherAssert.assertThat(itemFromDb.getName(), Matchers.equalTo(item.getName()));
        MatcherAssert.assertThat(itemFromDb.getDescription(), Matchers.equalTo(item.getDescription()));
        MatcherAssert.assertThat(itemFromDb.getAvailable(), Matchers.equalTo(item.getAvailable()));
        MatcherAssert.assertThat(itemFromDb.getOwner().getId(), Matchers.equalTo(item.getOwner().getId()));
    }

    @Test
    void updateItemAvailabilityStringTest() {
        Item updatedItem = Item.builder()
                .id(item.getId())
                .owner(item.getOwner())
                .available(false)
                .build();
        itemService.updateItem(updatedItem);

        Item itemFromDb = itemService.getItemById(item.getId(), user.getId());

        MatcherAssert.assertThat(itemFromDb.getId(), Matchers.equalTo(item.getId()));
        MatcherAssert.assertThat(itemFromDb.getName(), Matchers.equalTo(item.getName()));
        MatcherAssert.assertThat(itemFromDb.getDescription(), Matchers.equalTo(item.getDescription()));
        MatcherAssert.assertThat(itemFromDb.getAvailable(), Matchers.equalTo(updatedItem.getAvailable()));
        MatcherAssert.assertThat(itemFromDb.getOwner().getId(), Matchers.equalTo(item.getOwner().getId()));
    }

    @Test
    void addCommentToItemTest() {
        Booking booking = Booking.builder()
                .item(item)
                .booker(user)
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(1))
                .status(BookingStatus.WAITING)
                .build();
        bookingService.createBooking(booking);

        Comment comment = Comment.builder()
                .text(StringGenerator.generateCommentText())
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        itemService.addCommentToItem(comment);
        MatcherAssert.assertThat(comment.getId(), Matchers.notNullValue());
    }

    @Test
    void addCommentToUnbookedItemTest() {
        Item otherItem = Item.builder()
                .name(StringGenerator.generateItemName())
                .description(StringGenerator.generateItemDescription())
                .available(true)
                .owner(user)
                .build();
        itemService.createItem(otherItem);

        Comment comment = Comment.builder()
                .text(StringGenerator.generateCommentText())
                .author(user)
                .item(otherItem)
                .created(LocalDateTime.now())
                .build();

        Assertions.assertThrows(CommentException.class, () -> itemService.addCommentToItem(comment));
    }

    @Test
    void addCommentToNotReturnedItemTest() {
        Booking booking = Booking.builder()
                .item(item)
                .booker(user)
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().plusDays(3))
                .status(BookingStatus.WAITING)
                .build();
        bookingService.createBooking(booking);

        Comment comment = Comment.builder()
                .text(StringGenerator.generateCommentText())
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        Assertions.assertThrows(CommentException.class, () -> itemService.addCommentToItem(comment));
    }
}
