package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import ru.yandex.practicum.filmorate.service.FilmService;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    @Autowired
    private FilmService films;

    @PostMapping
    public Film add(@RequestBody Film film) {
        films.add(film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        films.update(film);
        return film;
    }

    @GetMapping
    public List<Film> get() {
        return films.get();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return films.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        films.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        films.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopular(@RequestParam Integer count) {
        if (count == null) {
            count = 10;
        }
        return films.getMostPopular(count);
    }
}
