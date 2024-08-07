package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.Rating;
import java.util.List;

public interface RatingStorage {
    List<Rating> get();

    Rating get(Integer id);

    boolean check(Rating mpa);
}
