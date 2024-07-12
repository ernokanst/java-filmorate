package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.JdbcRatingStorage;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {JdbcRatingStorage.class})
public class RatingTest {

    private final JdbcRatingStorage ratings;

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
