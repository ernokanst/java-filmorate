package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.JdbcGenreStorage;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {JdbcGenreStorage.class})
public class GenreTest {

    private final JdbcGenreStorage genres;

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
}
