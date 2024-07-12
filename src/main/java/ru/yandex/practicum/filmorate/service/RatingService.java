package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;

@Service
public class RatingService {

    private RatingStorage ratingStorage;

    @Autowired
    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public List<Rating> get() {
        return ratingStorage.get();
    }

    public Rating get(Integer id) {
        if (ratingStorage.get(id) == null) {
            throw new NotFoundException("Рейтинг не найден");
        }
        return ratingStorage.get(id);
    }
}
