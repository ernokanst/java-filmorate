package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.*;

@Service
public class TheFilmService implements FilmService {
    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;
    @Autowired
    private ValidateService validateService;
    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    public Film add(Film film) {
        validateService.checkFilm(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validateService.checkFilm(film);
        if (filmStorage.get(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return filmStorage.update(film);
    }

    public List<Film> get() {
        return filmStorage.get();
    }

    public Film get(Integer id) {
        if (filmStorage.get(id) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return filmStorage.get(id);
    }

    public void addLike(Integer id, Integer userId) {
        if (filmStorage.get(id) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (userStorage.get(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        if (filmStorage.get(id) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (userStorage.get(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getMostPopular(Integer count) {
        return filmStorage.getMostPopular(count);
    }
}