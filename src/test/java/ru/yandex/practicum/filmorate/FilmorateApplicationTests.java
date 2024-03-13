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
	public void filmEmptyName() {
		Film film = new Film(null, "A film without name", LocalDate.now(), 60);
		assertThrows(ValidationException.class, () -> films.addMovie(film));
		film.setName("");
		assertThrows(ValidationException.class, () -> films.addMovie(film));
		film.setName(" ");
		assertThrows(ValidationException.class, () -> films.addMovie(film));
	}

	@Test
	public void filmLongDescription() {
		Film film = new Film("Bee Movie",
				"According to all known laws of aviation, there is no way a bee should be able to fly. Its wings are too small to get its fat little body off the ground. The bee, of course, flies anyway because bees don't care what humans think is impossible.",
				LocalDate.of(2007, 11,2), 91);
		assertThrows(ValidationException.class, () -> films.addMovie(film));
		film.setDescription("B".repeat(201));
		assertThrows(ValidationException.class, () -> films.addMovie(film));
		film.setDescription("B".repeat(200));
		assertDoesNotThrow(() -> films.addMovie(film));
	}

	@Test
	public void filmReleaseDate() {
		Film film = new Film("Film", "A film with wrong date", LocalDate.of(1895, 12, 27), 60);
		assertThrows(ValidationException.class, () -> films.addMovie(film));
		film.setReleaseDate(LocalDate.of(1895, 12, 28));
		assertDoesNotThrow(() -> films.addMovie(film));
	}

	@Test
	public void filmNegativeDuration() {
		Film film = new Film("Film", "A film with wrong duration", LocalDate.now(), -60);
		assertThrows(ValidationException.class, () -> films.addMovie(film));
		film.setDuration(0);
		assertThrows(ValidationException.class, () -> films.addMovie(film));
		film.setDuration(1);
		assertDoesNotThrow(() -> films.addMovie(film));
	}

	@Test
	public void userWrongEmail() {
		User user = new User(null, "user", "John Doe", LocalDate.of(2000, 1, 1));
		assertThrows(ValidationException.class, () -> users.addUser(user));
		user.setEmail("");
		assertThrows(ValidationException.class, () -> users.addUser(user));
		user.setEmail("email");
		assertThrows(ValidationException.class, () -> users.addUser(user));
		user.setEmail("email@email.com");
		assertDoesNotThrow(() -> users.addUser(user));
	}

	@Test
	public void userWrongLogin() {
		User user = new User("email@email.com", null, "John Doe", LocalDate.of(2000, 1, 1));
		assertThrows(ValidationException.class, () -> users.addUser(user));
		user.setLogin("");
		assertThrows(ValidationException.class, () -> users.addUser(user));
		user.setLogin(" ");
		assertThrows(ValidationException.class, () -> users.addUser(user));
		user.setLogin("a user");
		assertThrows(ValidationException.class, () -> users.addUser(user));
		user.setLogin("user");
		user.setName(null);
		users.addUser(user);
		assertEquals(users.getUser().stream().findFirst().get().getName(), user.getLogin());
	}

	@Test
	public void userDOBinFuture() {
		User user = new User("email@email.com", "user", "John Doe", LocalDate.of(2222, 2, 22));
		assertThrows(ValidationException.class, () -> users.addUser(user));
		user.setBirthday(LocalDate.now());
		assertDoesNotThrow(() -> users.addUser(user));
	}
}
