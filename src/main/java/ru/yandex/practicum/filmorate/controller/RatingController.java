package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class RatingController {
    @Autowired
    RatingService ratingService;

    @GetMapping
    public List<Rating> get() {
        return ratingService.get();
    }

    @GetMapping("/{id}")
    public Rating getGenre(@PathVariable Integer id) {
        return ratingService.get(id);
    }
}
