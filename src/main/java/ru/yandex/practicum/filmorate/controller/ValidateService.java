package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.*;
import java.time.LocalDate;

public class ValidateService {
    public static boolean checkFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            return false;
        }
        if (film.getDescription().length() > 200) {
            return false;
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return false;
        }
        if (film.getDuration() < 1) {
            return false;
        }
        return true;
    }

    public static boolean checkUser(User user) {
        if (user.getEmail() == null || user.getLogin() == null) {
            return false;
        }
        if (!(user.getEmail().contains("@"))) {
            return false;
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            return false;
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            return false;
        }
        return true;
    }
}
