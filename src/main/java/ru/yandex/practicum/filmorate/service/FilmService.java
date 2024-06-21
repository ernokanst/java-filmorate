package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public void addLike(Film film, Integer user) {
        film.addLike(user);
    }

    public void removeLike(Film film, Integer user) {
        film.removeLike(user);
    }

    public List<Film> getMostPopular(Collection<Film> films, int count) {
        return films.stream().sorted((x1, x2) -> x2.getLikes().size() - x1.getLikes().size()).limit(count).collect(Collectors.toList());
    }
}
