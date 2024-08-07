package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import java.time.LocalDate;

@Component
public class ValidateService {

    public void checkFilm(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не должно быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть меньше 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Фильм не может быть старше 1895 года");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }

    public void checkUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getLogin() == null) {
            throw new ValidationException("Имя и логин не могут быть пустыми");
        }
        if (!(user.getEmail().contains("@"))) {
            throw new ValidationException("Почта указана неверно");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения не может быть в будущем");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    public void checkUpdateUser(User user) throws ValidationException {
        if (user.getId() == null) {
            throw new ValidationException("Отсутстсвует идентификатор пользователя");
        }
        checkUser(user);
    }
}
