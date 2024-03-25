package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.ValidateService;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
        if (validator.checkFilm(film)) {
            film.setId(currentId++);
            films.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film, HttpServletResponse response) {
        if (validator.checkFilm(film)) {
            if (films.containsKey(film.getId())) {
                films.replace(film.getId(), film);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        return film;
    }

    @GetMapping
    public List<Film> get() {
        return new ArrayList<>(films.values());
    }
}
