package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.ValidateService;
import java.util.*;
import org.springframework.security.acls.model.NotFoundException;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Map<Integer, Film> films = new LinkedHashMap<>();
    private int currentId = 1;
    private ValidateService validator;

    @Autowired
    public FilmController(ValidateService validateService) {
        validator = validateService;
    }

    @PostMapping
    public Film add(@RequestBody Film film) {
        validator.checkFilm(film);
        film.setId(currentId++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        validator.checkFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }
        films.replace(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> get() {
        return new ArrayList<>(films.values());
    }
}
