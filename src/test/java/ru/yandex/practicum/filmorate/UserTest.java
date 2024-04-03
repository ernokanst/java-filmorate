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
    public void allCorrectTest() {
        User user = new User("email@email.com", "user", "John Doe", LocalDate.of(2000, 1, 1));
        assertDoesNotThrow(() -> users.add(user));
    }

    @Test
    public void wrongEmailTest() {
        User user = new User(null, "user", "John Doe", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> users.add(user));
        user.setEmail("");
        assertThrows(ValidationException.class, () -> users.add(user));
        user.setEmail("email");
        assertThrows(ValidationException.class, () -> users.add(user));
        user.setEmail("email@email.com");
        assertDoesNotThrow(() -> users.add(user));
    }

    @Test
    public void wrongLoginTest() {
        User user = new User("email@email.com", null, "John Doe", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> users.add(user));
        user.setLogin("");
        assertThrows(ValidationException.class, () -> users.add(user));
        user.setLogin(" ");
        assertThrows(ValidationException.class, () -> users.add(user));
        user.setLogin("a user");
        assertThrows(ValidationException.class, () -> users.add(user));
    }

    @Test
    public void noNameTest() {
        User user = new User("email@email.com", "user", null, LocalDate.of(2000, 1, 1));
        users.add(user);
        assertEquals(users.get().stream().filter(u -> u.getId() == 1).findFirst().get().getName(), user.getLogin());
    }

    @Test
    public void wrongBirthdateTest() {
        User user3 = new User("email@email.com", "user", "John Doe", LocalDate.of(2222, 2, 22));
        assertThrows(ValidationException.class, () -> users.add(user3));
        user3.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> users.add(user3));
    }
}
