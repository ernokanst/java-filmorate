package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

	FilmController films = new FilmController();
	UserController users = new UserController();

	@Test
	public void filmTest() {
		Film film1 = new Film(null, "A film without name", LocalDate.now(), 60);
		assertThrows(ValidationException.class, () -> films.add(film1));
		film1.setName("");
		assertThrows(ValidationException.class, () -> films.add(film1));
		film1.setName(" ");
		assertThrows(ValidationException.class, () -> films.add(film1));
		Film film2 = new Film("Bee Movie",
				"According to all known laws of aviation, there is no way a bee should be able to fly. Its wings are too small to get its fat little body off the ground. The bee, of course, flies anyway because bees don't care what humans think is impossible.",
				LocalDate.of(2007, 11,2), 91);
		assertThrows(ValidationException.class, () -> films.add(film2));
		film2.setDescription("B".repeat(201));
		assertThrows(ValidationException.class, () -> films.add(film2));
		film2.setDescription("B".repeat(200));
		assertDoesNotThrow(() -> films.add(film2));
		Film film3 = new Film("Film", "A film with wrong date", LocalDate.of(1895, 12, 27), 60);
		assertThrows(ValidationException.class, () -> films.add(film3));
		film3.setReleaseDate(LocalDate.of(1895, 12, 28));
		assertDoesNotThrow(() -> films.add(film3));
		Film film4 = new Film("Film", "A film with wrong duration", LocalDate.now(), -60);
		assertThrows(ValidationException.class, () -> films.add(film4));
		film4.setDuration(0);
		assertThrows(ValidationException.class, () -> films.add(film4));
		film4.setDuration(1);
		assertDoesNotThrow(() -> films.add(film4));
	}

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
