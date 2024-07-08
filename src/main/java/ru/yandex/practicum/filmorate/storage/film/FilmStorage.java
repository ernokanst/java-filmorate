package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    Map<Integer, Film> get();

    Film get(Integer id);

    void addLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    List<Film> getMostPopular(int count);
}
