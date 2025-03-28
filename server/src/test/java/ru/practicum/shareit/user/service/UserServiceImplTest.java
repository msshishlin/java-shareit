package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.generator.StringGenerator;
import ru.practicum.shareit.user.model.User;

import java.util.List;

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
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", user.getId());

        User userFromDb = query.getSingleResult();
        User userFromService = userService.getUserById(user.getId());

        MatcherAssert.assertThat(userFromService.getId(), Matchers.equalTo(userFromDb.getId()));
        MatcherAssert.assertThat(userFromService.getName(), Matchers.equalTo(userFromDb.getName()));
        MatcherAssert.assertThat(userFromService.getEmail(), Matchers.equalTo(userFromDb.getEmail()));
    }

    @Test
    void updateUserTest() {
        User updatedUser = User.builder()
                .id(user.getId())
                .name(StringGenerator.generateUserName())
                .email(StringGenerator.generateUserEmail())
                .build();

        userService.updateUser(updatedUser);

        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", user.getId());

        User userFromDb = query.getSingleResult();

        MatcherAssert.assertThat(userFromDb.getId(), Matchers.equalTo(updatedUser.getId()));
        MatcherAssert.assertThat(userFromDb.getName(), Matchers.equalTo(updatedUser.getName()));
        MatcherAssert.assertThat(userFromDb.getEmail(), Matchers.equalTo(updatedUser.getEmail()));
    }

    @Test
    void deleteUserTest() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", user.getId());

        User userBeforeDelete = query.getSingleResult();
        MatcherAssert.assertThat(userBeforeDelete, Matchers.notNullValue());

        userService.deleteUser(user.getId());

        List<User> userAfterDelete = query.getResultList();
        MatcherAssert.assertThat(userAfterDelete.size(), Matchers.equalTo(0));
    }
}
