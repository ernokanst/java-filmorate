package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.*;

@Service
public class TheFilmService implements FilmService {

    private FilmStorage filmStorage;
    private ValidateService validateService;
    private UserStorage userStorage;
    private GenreStorage genreStorage;
    private RatingStorage ratingStorage;

    @Autowired
    public TheFilmService(FilmStorage filmStorage, ValidateService validateService, UserStorage userStorage, GenreStorage genreStorage, RatingStorage ratingStorage) {
        this.filmStorage = filmStorage;
        this.validateService = validateService;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
    }

    public Film add(Film film) {
        validateService.checkFilm(film);
        if (film.getGenres() != null && !genreStorage.check(film.getGenres())) {
            throw new ValidationException("Жанр(ы) указан(ы) неверно");
        }
        if (film.getMpa() != null && !ratingStorage.check(film.getMpa())) {
            throw new ValidationException("Рейтинг указан неверно");
        }
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validateService.checkFilm(film);
        if (filmStorage.get(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (film.getGenres() != null && !genreStorage.check(film.getGenres())) {
            throw new ValidationException("Жанр(ы) указан(ы) неверно");
        }
        if (film.getMpa() != null && !ratingStorage.check(film.getMpa())) {
            throw new ValidationException("Рейтинг указан неверно");
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