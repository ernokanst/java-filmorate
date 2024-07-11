package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    @Autowired
    GenreStorage genreStorage;

    public List<Genre> get() {
        return genreStorage.get();
    }

    public Genre get(Integer id) {
        if (id > 6 || id < 1) {
            throw new NotFoundException("Жанр не найден");
        }
        return genreStorage.get(id);
    }
}
