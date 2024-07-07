package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import org.springframework.context.ApplicationContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    private ApplicationContext context;

    public List<Film> getMostPopular(int count) {
        List<Film> films = context.getBean(InMemoryFilmStorage.class).get();
        Map<Integer, Set<Integer>> likes = context.getBean(InMemoryFilmStorage.class).getLikes();
        return films.stream().sorted((x1, x2) -> likes.get(x2.getId()).size() - likes.get(x1.getId()).size()).limit(count).collect(Collectors.toList());
    }
}