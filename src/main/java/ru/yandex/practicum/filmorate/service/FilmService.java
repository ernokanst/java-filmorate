package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public void addLike(Film film, User user) {
        film.addLike(user.getId());
    }

    public void removeLike(Film film, User user) {
        film.removeLike(user.getId());
    }

    public List<Film> getMostPopular(List<Film> films) {
        return films.stream().sorted((x1, x2) -> x2.getLikes().size() - x1.getLikes().size()).limit(10).collect(Collectors.toList());
    }
}
