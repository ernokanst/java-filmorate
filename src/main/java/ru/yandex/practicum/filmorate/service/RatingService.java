package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    RatingStorage ratingStorage;

    public List<Rating> get() {
        return ratingStorage.get();
    }

    public Rating get(Integer id) {
        if (id > 5 || id < 1) {
            throw new NotFoundException("Рейтинг не найден");
        }
        return ratingStorage.get(id);
    }
}
