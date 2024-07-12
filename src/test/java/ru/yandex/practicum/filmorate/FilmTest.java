package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.JdbcGenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.JdbcRatingStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.film.JdbcFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.JdbcUserStorage;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {JdbcFilmStorage.class, JdbcUserStorage.class, JdbcGenreStorage.class, JdbcRatingStorage.class})
class FilmTest {

	private final JdbcFilmStorage films;
	private final JdbcUserStorage users;

	@Test
	public void testAdd() {
		Film film = new Film("Movie", "A movie", LocalDate.now(), 60, Set.of(new Genre(1, "Комедия")), new Rating(1, "G"));
		Film addedFilm = films.add(film);
		assertEquals(1, addedFilm.getId());
	}

	@Test
	public void testUpdate() {
		Film film = new Film("Movie", "A movie", LocalDate.now(), 60, Set.of(new Genre(1, "Комедия")), new Rating(1, "G"));
		film = films.add(film);
		film.setName("New movie");
		film.setDescription("A movie about something");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(100);
		film.setMpa(new Rating(5, "NC-17"));
		film.setGenres(Set.of(new Genre(6, "Боевик")));
		Film addedFilm = films.update(film);
		assertEquals(film, addedFilm);
	}

	@Test
	public void testGetAll() {
		Film film1 = new Film("Movie", "A movie", LocalDate.now(), 60, Set.of(new Genre(1, "Комедия")), new Rating(1, "G"));
		Film film2 = new Film("New movie", "A movie about something", LocalDate.now(), 100, Set.of(new Genre(6, "Боевик")), new Rating(5, "NC-17"));
		Film film3 = new Film("Movie the third", "A movie about something else", LocalDate.now(), 90, Set.of(new Genre(4, "Триллер"), new Genre(5, "Документальный")), new Rating(3, "PG-13"));
		films.add(film1);
		films.add(film2);
		films.add(film3);
		List<Film> getFilms = films.get();
		assertEquals(getFilms, List.of(film1, film2, film3));
	}

	@Test
	public void testGetOne() {
		Film film1 = new Film("Movie", "A movie", LocalDate.now(), 60, Set.of(new Genre(1, "Комедия")), new Rating(1, "G"));
		Film film2 = new Film("New movie", "A movie about something", LocalDate.now(), 100, Set.of(new Genre(6, "Боевик")), new Rating(5, "NC-17"));
		Film film3 = new Film("Movie the third", "A movie about something else", LocalDate.now(), 90, Set.of(new Genre(4, "Триллер"), new Genre(5, "Документальный")), new Rating(3, "PG-13"));
		films.add(film1);
		film2.setId(films.add(film2).getId());
		films.add(film3);
		Film getFilm = films.get(film2.getId());
		assertEquals(getFilm, film2);
	}

	@Test
	public void testLikes() {
		Film film1 = new Film("Movie", "A movie", LocalDate.now(), 60, Set.of(new Genre(1, "Комедия")), new Rating(1, "G"));
		Film film2 = new Film("New movie", "A movie about something", LocalDate.now(), 100, Set.of(new Genre(6, "Боевик")), new Rating(5, "NC-17"));
		Film film3 = new Film("Movie the third", "A movie about something else", LocalDate.now(), 90, Set.of(new Genre(4, "Триллер"), new Genre(5, "Документальный")), new Rating(3, "PG-13"));
		User user1 = new User("email@email.com", "user", "John Doe", LocalDate.of(2000, 1, 1));
		User user2 = new User("diffirent_email@email.com", "login", "Jane Doe", LocalDate.of(1987, 6, 5));
		film1.setId(films.add(film1).getId());
		film2.setId(films.add(film2).getId());
		film3.setId(films.add(film3).getId());
		user1.setId(users.add(user1).getId());
		user2.setId(users.add(user2).getId());
		films.addLike(film3.getId(), user1.getId());
		films.addLike(film3.getId(), user2.getId());
		films.addLike(film2.getId(), user1.getId());
		List<Film> mostPopular = films.getMostPopular(3);
		assertEquals(mostPopular, List.of(film3, film2, film1));
		assertNotEquals(mostPopular, List.of(film1, film2, film3));
		mostPopular = films.getMostPopular(1000);
		assertEquals(mostPopular, List.of(film3, film2, film1));
		films.deleteLike(film3.getId(), user1.getId());
		films.deleteLike(film3.getId(), user2.getId());
		mostPopular = films.getMostPopular(3);
		assertEquals(mostPopular, List.of(film2, film1, film3));
	}
}
