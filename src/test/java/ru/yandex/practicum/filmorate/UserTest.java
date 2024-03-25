package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {

    ValidateService validateService = new ValidateService();
    UserController users = new UserController(validateService);

    @Test
    public void userTest() {
        User user1 = new User(null, "user", "John Doe", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> users.add(user1));
        user1.setEmail("");
        assertThrows(ValidationException.class, () -> users.add(user1));
        user1.setEmail("email");
        assertThrows(ValidationException.class, () -> users.add(user1));
        user1.setEmail("email@email.com");
        assertDoesNotThrow(() -> users.add(user1));
        User user2 = new User("email@email.com", null, "John Doe", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> users.add(user2));
        user2.setLogin("");
        assertThrows(ValidationException.class, () -> users.add(user2));
        user2.setLogin(" ");
        assertThrows(ValidationException.class, () -> users.add(user2));
        user2.setLogin("a user");
        assertThrows(ValidationException.class, () -> users.add(user2));
        user2.setLogin("user");
        user2.setName(null);
        users.add(user2);
        assertEquals(users.get().stream().filter(u -> u.getId() == 2).findFirst().get().getName(), user2.getLogin());
        User user3 = new User("email@email.com", "user", "John Doe", LocalDate.of(2222, 2, 22));
        assertThrows(ValidationException.class, () -> users.add(user3));
        user3.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> users.add(user3));
    }
}
