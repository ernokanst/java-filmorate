package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Map<Integer, Film> films = new LinkedHashMap<>();
    private int currentId = 1;

    @PostMapping
    public Film add(@RequestBody Film film) {
        if (ValidateService.checkFilm(film)) {
            film.setId(currentId++);
            films.put(film.getId(), film);
            return film;
        }
        throw new ValidationException("Ошибка создания фильма");
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (ValidateService.checkFilm(film)) {
            if (films.containsKey(film.getId())) {
                films.replace(film.getId(), film);
                return film;
            }
        }
        throw new ValidationException("Ошибка обновления фильма");
    }

    @GetMapping
    public List<Film> get() {
        return new ArrayList<>(films.values());
    }
}
