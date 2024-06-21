package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.ValidateService;
import java.time.LocalDate;

@SpringBootTest
class FilmTest {

	ValidateService validateService = new ValidateService();
	FilmService filmService = new FilmService();
	FilmStorage films = new InMemoryFilmStorage(validateService, filmService);

	@Test
	public void allCorrectTest() {
		Film film = new Film("Movie", "A movie", LocalDate.now(), 60);
		assertDoesNotThrow(() -> films.add(film));
	}

	@Test
	public void emptyNameTest() {
		Film film = new Film(null, "A film without name", LocalDate.now(), 60);
		assertThrows(ValidationException.class, () -> films.add(film));
		film.setName("");
		assertThrows(ValidationException.class, () -> films.add(film));
		film.setName(" ");
		assertThrows(ValidationException.class, () -> films.add(film));
	}

	@Test
	public void wrongDescriptionTest() {
		Film film = new Film("Bee Movie",
				"According to all known laws of aviation, there is no way a bee should be able to fly. Its wings are too small to get its fat little body off the ground. The bee, of course, flies anyway because bees don't care what humans think is impossible.",
				LocalDate.of(2007, 11,2), 91);
		assertThrows(ValidationException.class, () -> films.add(film));
		film.setDescription("B".repeat(201));
		assertThrows(ValidationException.class, () -> films.add(film));
		film.setDescription("B".repeat(200));
		assertDoesNotThrow(() -> films.add(film));
	}

	@Test
	public void wrongDateTest() {
		Film film = new Film("Film", "A film with wrong date", LocalDate.of(1895, 12, 27), 60);
		assertThrows(ValidationException.class, () -> films.add(film));
		film.setReleaseDate(LocalDate.of(1895, 12, 28));
		assertDoesNotThrow(() -> films.add(film));
	}

	@Test
	public void wrongDurationTest() {
		Film film = new Film("Film", "A film with wrong duration", LocalDate.now(), -60);
		assertThrows(ValidationException.class, () -> films.add(film));
		film.setDuration(0);
		assertThrows(ValidationException.class, () -> films.add(film));
		film.setDuration(1);
		assertDoesNotThrow(() -> films.add(film));
	}
}
