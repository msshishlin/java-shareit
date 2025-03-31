package ru.practicum.shareit.user.service;

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
import ru.practicum.shareit.user.model.User;

/**
 * Тесты сервиса для работы с пользователями.
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public final class UserServiceImplTest {
    private final EntityManager entityManager;

    /**
     * Сервисы для работы с пользователями.
     */
    private final UserService userService;

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
    }

    @Test
    void createUserTest() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", user.getEmail());

        User userFromDb = query.getSingleResult();

        MatcherAssert.assertThat(userFromDb.getId(), CoreMatchers.not(0));
        MatcherAssert.assertThat(userFromDb.getName(), Matchers.equalTo(user.getName()));
        MatcherAssert.assertThat(userFromDb.getEmail(), Matchers.equalTo(user.getEmail()));
    }

    @Test
    void getUserByIdTest() {
        User userFromService = userService.getUserById(user.getId());

        MatcherAssert.assertThat(userFromService.getId(), Matchers.equalTo(user.getId()));
        MatcherAssert.assertThat(userFromService.getName(), Matchers.equalTo(user.getName()));
        MatcherAssert.assertThat(userFromService.getEmail(), Matchers.equalTo(user.getEmail()));
    }

    @Test()
    void getNonExistentUserById() {
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(Long.MAX_VALUE));
    }

    @Test
    void updateUserNameTest() {
        User updatedUser = User.builder()
                .id(user.getId())
                .name(StringGenerator.generateUserName())
                .build();
        userService.updateUser(updatedUser);

        User userFromDb = userService.getUserById(user.getId());

        MatcherAssert.assertThat(userFromDb.getId(), Matchers.equalTo(user.getId()));
        MatcherAssert.assertThat(userFromDb.getName(), Matchers.equalTo(updatedUser.getName()));
        MatcherAssert.assertThat(userFromDb.getEmail(), Matchers.equalTo(user.getEmail()));
    }

    @Test
    void updateUserNameToBlankStringTest() {
        User updatedUser = User.builder()
                .id(user.getId())
                .name("   ")
                .build();
        userService.updateUser(updatedUser);

        User userFromDb = userService.getUserById(user.getId());

        MatcherAssert.assertThat(userFromDb.getId(), Matchers.equalTo(user.getId()));
        MatcherAssert.assertThat(userFromDb.getName(), Matchers.equalTo(user.getName()));
        MatcherAssert.assertThat(userFromDb.getEmail(), Matchers.equalTo(user.getEmail()));
    }

    @Test
    void updateUserEmailTest() {
        User updatedUser = User.builder()
                .id(user.getId())
                .email(StringGenerator.generateUserEmail())
                .build();
        userService.updateUser(updatedUser);

        User userFromDb = userService.getUserById(user.getId());

        MatcherAssert.assertThat(userFromDb.getId(), Matchers.equalTo(user.getId()));
        MatcherAssert.assertThat(userFromDb.getName(), Matchers.equalTo(user.getName()));
        MatcherAssert.assertThat(userFromDb.getEmail(), Matchers.equalTo(updatedUser.getEmail()));
    }

    @Test
    void updateUserEmailToBlankStringTest() {
        User updatedUser = User.builder()
                .id(user.getId())
                .email("   ")
                .build();
        userService.updateUser(updatedUser);

        User userFromDb = userService.getUserById(user.getId());

        MatcherAssert.assertThat(userFromDb.getId(), Matchers.equalTo(user.getId()));
        MatcherAssert.assertThat(userFromDb.getName(), Matchers.equalTo(user.getName()));
        MatcherAssert.assertThat(userFromDb.getEmail(), Matchers.equalTo(user.getEmail()));
    }

    @Test
    void deleteUserTest() {
        User userBeforeDelete = userService.getUserById(user.getId());
        MatcherAssert.assertThat(userBeforeDelete, Matchers.notNullValue());

        userService.deleteUser(user.getId());
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    void deleteNonExistentUserTest() {
        Assertions.assertThrows(NotFoundException.class, () -> userService.deleteUser(Long.MAX_VALUE));
    }
}
