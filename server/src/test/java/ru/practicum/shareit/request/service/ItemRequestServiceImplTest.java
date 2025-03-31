package ru.practicum.shareit.request.service;

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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.generator.StringGenerator;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Тесты сервиса для работы с запросами вещей.
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class ItemRequestServiceImplTest {
    private final EntityManager entityManager;

    /**
     * Сервис для работы с запросами вещей.
     */
    private final ItemRequestService itemRequestService;

    /**
     * Сервисы для работы с пользователями.
     */
    private final UserService userService;

    /**
     * Запрос вещи.
     */
    private ItemRequest itemRequest;

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

        itemRequest = ItemRequest.builder()
                .requester(user)
                .description(StringGenerator.generateItemRequestDescription())
                .created(LocalDateTime.now())
                .build();
        itemRequestService.createItemRequest(itemRequest);
    }

    @Test
    void createItemRequestTest() {
        TypedQuery<ItemRequest> query = entityManager.createQuery("SELECT ir FROM ItemRequest ir WHERE ir.description = :description", ItemRequest.class)
                .setParameter("description", itemRequest.getDescription());

        ItemRequest itemRequestFromDb = query.getSingleResult();

        MatcherAssert.assertThat(itemRequestFromDb.getId(), CoreMatchers.not(0));
        MatcherAssert.assertThat(itemRequestFromDb.getDescription(), Matchers.equalTo(itemRequest.getDescription()));
        MatcherAssert.assertThat(itemRequestFromDb.getCreated(), Matchers.equalTo(itemRequest.getCreated()));
    }

    @Test
    void getUserItemRequestsTest() {
        for (int i = 0; i < 4; i++) {
            ItemRequest otherItemRequest = ItemRequest.builder()
                    .requester(user)
                    .description(StringGenerator.generateItemRequestDescription())
                    .created(LocalDateTime.now())
                    .build();
            itemRequestService.createItemRequest(otherItemRequest);
        }

        Collection<ItemRequest> userItemRequests = itemRequestService.getUserItemRequests(user.getId());
        MatcherAssert.assertThat(userItemRequests.size(), Matchers.equalTo(5));
    }

    @Test
    void getUserItemRequestsForNonExistentUserTest() {
        Assertions.assertThrows(NotFoundException.class, () -> itemRequestService.getUserItemRequests(Long.MAX_VALUE));
    }

    @Test
    void getOtherUsersItemRequestsTest() {
        for (int i = 0; i < 10; i++) {
            User otherUser = User.builder()
                    .name(StringGenerator.generateUserName())
                    .email(StringGenerator.generateUserEmail())
                    .build();
            userService.createUser(otherUser);

            ItemRequest otherItemRequest = ItemRequest.builder()
                    .requester(otherUser)
                    .description(StringGenerator.generateItemRequestDescription())
                    .created(LocalDateTime.now())
                    .build();
            itemRequestService.createItemRequest(otherItemRequest);
        }

        Collection<ItemRequest> userItemRequests = itemRequestService.getOtherUsersItemRequests(user.getId());
        MatcherAssert.assertThat(userItemRequests.size(), Matchers.equalTo(10));
    }

    @Test
    void getRequestTest() {
        ItemRequestWithItemsDto itemRequestFromDb = itemRequestService.getRequest(user.getId(), itemRequest.getId());

        MatcherAssert.assertThat(itemRequestFromDb.getId(), Matchers.equalTo(itemRequest.getId()));
        MatcherAssert.assertThat(itemRequestFromDb.getDescription(), Matchers.equalTo(itemRequest.getDescription()));
        MatcherAssert.assertThat(itemRequestFromDb.getCreated(), Matchers.equalTo(itemRequest.getCreated()));
    }
}
