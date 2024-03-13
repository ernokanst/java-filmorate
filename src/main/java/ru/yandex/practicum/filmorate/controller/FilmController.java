package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int currentID = 1;

    @PostMapping
    public Film addMovie(@RequestBody Film film) {
        if (!(film.getName().isEmpty())) {
          if (film.getDescription().length() <= 200) {
              if (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
                  if (film.getDuration() > 0) {
                      film.setId(currentID++);
                      films.put(film.getId(), film);
                  } else {
                      throw new ValidationException("Продолжительность фильма не может быть отрицательной");
                  }
              } else {
                  throw new ValidationException("Фильм не может быть старше 1895 года");
              }
          } else {
              throw new ValidationException("Описание должно быть меньше 200 символов");
          }
        } else {
            throw new ValidationException("Название не должно быть пустым");
        }
        return film;
    }

    @PutMapping
    public Film updateMovie(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
        } else {
            throw new ValidationException("Ошибка обновления фильма");
        }
        return film;
    }

    @GetMapping
    public List<Film> getMovie() {
        return new ArrayList<>(films.values());
    }
}
