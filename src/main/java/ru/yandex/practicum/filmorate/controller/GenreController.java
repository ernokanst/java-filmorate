package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    @Autowired
    GenreService genreService;

    @GetMapping
    public List<Genre> get() {
        return genreService.get();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable Integer id) {
        return genreService.get(id);
    }
}
