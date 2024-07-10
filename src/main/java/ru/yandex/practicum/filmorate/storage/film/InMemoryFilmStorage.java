package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new LinkedHashMap<>();
    private Map<Integer, Set<Integer>> likes = new LinkedHashMap<>();
    private int currentId = 1;

    public Film add(Film film) {
        film.setId(currentId++);
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        return film;
    }

    public Film update(Film film) {
        films.replace(film.getId(), film);
        return film;
    }

    public List<Film> get() {
        return new ArrayList<>(films.values());
    }

    public Film get(Integer id) {
        return films.get(id);
    }

    public void addLike(Integer id, Integer userId) {
        likes.get(id).add(userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        likes.get(id).remove(userId);
    }

    public List<Film> getMostPopular(int count) {
        return films.values().stream().sorted((x1, x2) -> likes.get(x2.getId()).size() - likes.get(x1.getId()).size()).limit(count).collect(Collectors.toList());
    }
}
