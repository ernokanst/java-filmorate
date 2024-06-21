package ru.yandex.practicum.filmorate.storage.film;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import java.util.*;
import org.springframework.security.acls.model.NotFoundException;

@RestController
@RequestMapping("/films")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    private Map<Integer, Film> films = new LinkedHashMap<>();
    private int currentId = 1;
    private ValidateService validator;
    private FilmService service;

    @Autowired
    public InMemoryFilmStorage(ValidateService validateService, FilmService filmService) {
        validator = validateService;
        service = filmService;
    }

    @SneakyThrows
    @PostMapping
    public Film add(@RequestBody Film film) {
        validator.checkFilm(film);
        film.setId(currentId++);
        films.put(film.getId(), film);
        return film;
    }

    @SneakyThrows
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

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        return films.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        service.addLike(films.get(id), userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        service.removeLike(films.get(id), userId);
    }

    @GetMapping("/films/popular?count={count}")
    public List<Film> getMostPopular(@PathVariable Integer count) {
        int c = 10;
        if (count != null) {
            c = count;
        }
        return service.getMostPopular(films.values(), c);
    }
}
