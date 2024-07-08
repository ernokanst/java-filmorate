package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.*;

@Service
public class TheFilmService implements FilmService {
    @Autowired
    private FilmStorage films;
    @Autowired
    private ValidateService validator;
    @Autowired
    private UserStorage users;

    public Film add(Film film) {
        validator.checkFilm(film);
        return films.add(film);
    }

    public Film update(Film film) {
        validator.checkFilm(film);
        if (!films.get().containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }
        return films.update(film);
    }

    public List<Film> get() {
        return new ArrayList<>(films.get().values());
    }

    public Film get(Integer id) {
        if (!films.get().containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        return films.get(id);
    }

    public void addLike(Integer id, Integer userId) {
        if (!films.get().containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!users.get().containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        films.addLike(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        if (!films.get().containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!users.get().containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        films.deleteLike(id, userId);
    }

    public List<Film> getMostPopular(Integer count) {
        return films.getMostPopular(count);
    }
}