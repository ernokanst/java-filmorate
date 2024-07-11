package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import java.time.LocalDate;
import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmDbStorage.class, GenreStorage.class, RatingStorage.class, UserDbStorage.class})
class FilmTest {

	private final FilmDbStorage films;
	private final GenreStorage genres;
	private final RatingStorage ratings;
	private final UserDbStorage users;

	@Test
	public void testAdd() {
		Film film = new Film("Movie", "A movie", LocalDate.now(), 60, List.of(new Genre(1, "Комедия")), new Rating(1, "G"));
		Film addedFilm = films.add(film);
		assertEquals(1, addedFilm.getId());
	}

	@Test
	public void testUpdate() {
		Film film = new Film("Movie", "A movie", LocalDate.now(), 60, List.of(new Genre(1, "Комедия")), new Rating(1, "G"));
		film = films.add(film);
		film.setName("New movie");
		film.setDescription("A movie about something");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(100);
		film.setMpa(new Rating(5, "NC-17"));
		film.setGenres(List.of(new Genre(6, "Боевик")));
		Film addedFilm = films.update(film);
		assertEquals(film, addedFilm);
	}

	@Test
	public void testGetAll() {
		Film film1 = new Film("Movie", "A movie", LocalDate.now(), 60, List.of(new Genre(1, "Комедия")), new Rating(1, "G"));
		Film film2 = new Film("New movie", "A movie about something", LocalDate.now(), 100, List.of(new Genre(6, "Боевик")), new Rating(5, "NC-17"));
		Film film3 = new Film("Movie the third", "A movie about something else", LocalDate.now(), 90, List.of(new Genre(4, "Триллер"), new Genre(5, "Документальный")), new Rating(3, "PG-13"));
		films.add(film1);
		films.add(film2);
		films.add(film3);
		List<Film> getFilms = films.get();
		assertEquals(getFilms, List.of(film1, film2, film3));
	}

	@Test
	public void testGetOne() {
		Film film1 = new Film("Movie", "A movie", LocalDate.now(), 60, List.of(new Genre(1, "Комедия")), new Rating(1, "G"));
		Film film2 = new Film("New movie", "A movie about something", LocalDate.now(), 100, List.of(new Genre(6, "Боевик")), new Rating(5, "NC-17"));
		Film film3 = new Film("Movie the third", "A movie about something else", LocalDate.now(), 90, List.of(new Genre(4, "Триллер"), new Genre(5, "Документальный")), new Rating(3, "PG-13"));
		films.add(film1);
		film2.setId(films.add(film2).getId());
		films.add(film3);
		Film getFilm = films.get(film2.getId());
		assertEquals(getFilm, film2);
	}

	@Test
	public void testLikes() {
		Film film1 = new Film("Movie", "A movie", LocalDate.now(), 60, List.of(new Genre(1, "Комедия")), new Rating(1, "G"));
		Film film2 = new Film("New movie", "A movie about something", LocalDate.now(), 100, List.of(new Genre(6, "Боевик")), new Rating(5, "NC-17"));
		Film film3 = new Film("Movie the third", "A movie about something else", LocalDate.now(), 90, List.of(new Genre(4, "Триллер"), new Genre(5, "Документальный")), new Rating(3, "PG-13"));
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

	@Test
	public void testGetGenre() {
		Genre genre = new Genre(4, "Триллер");
		Genre getGenre = genres.get(4);
		assertEquals(getGenre, genre);
	}

	@Test
	public void testGetAllGenres() {
		Genre genre1 = new Genre(1, "Комедия");
		Genre genre2 = new Genre(2, "Драма");
		Genre genre3 = new Genre(3, "Мультфильм");
		Genre genre4 = new Genre(4, "Триллер");
		Genre genre5 = new Genre(5, "Документальный");
		Genre genre6 = new Genre(6, "Боевик");
		List<Genre> getGenres = genres.get();
		assertEquals(getGenres, List.of(genre1, genre2, genre3, genre4, genre5, genre6));
	}

	@Test
	public void testGetRating() {
		Rating mpa = new Rating(4, "R");
		Rating getMpa = ratings.get(4);
		assertEquals(getMpa, mpa);
	}

	@Test
	public void testGetAllRatings() {
		Rating mpa1 = new Rating(1, "G");
		Rating mpa2 = new Rating(2, "PG");
		Rating mpa3 = new Rating(3, "PG-13");
		Rating mpa4 = new Rating(4, "R");
		Rating mpa5 = new Rating(5, "NC-17");
		List<Rating> getRatings = ratings.get();
		assertEquals(getRatings, List.of(mpa1, mpa2, mpa3, mpa4, mpa5));
	}
}
