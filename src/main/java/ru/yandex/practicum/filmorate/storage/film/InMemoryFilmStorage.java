package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import java.util.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new LinkedHashMap<>();
    @Getter
    private Map<Integer, Set<Integer>> likes = new LinkedHashMap<>();
    private int currentId = 1;
    @Autowired
    private ValidateService validator;
    @Autowired
    private FilmService service;
    @Autowired
    private InMemoryUserStorage users;

    @SneakyThrows
    public Film add(Film film) {
        validator.checkFilm(film);
        film.setId(currentId++);
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        return film;
    }

    @SneakyThrows
    public Film update(Film film) {
        validator.checkFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }
        films.replace(film.getId(), film);
        return film;
    }

    public List<Film> get() {
        return new ArrayList<>(films.values());
    }

    public Film getFilm(Integer id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        return films.get(id);
    }

    public void addLike(Integer id, Integer userId) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!users.getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        likes.get(id).add(userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!users.getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        likes.get(id).remove(userId);
    }

    public List<Film> getMostPopular(Integer count) {
        int c = 10;
        if (count != null) {
            c = count;
        }
        return service.getMostPopular(c);
    }
}
