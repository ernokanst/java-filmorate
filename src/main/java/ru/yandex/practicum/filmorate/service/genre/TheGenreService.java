package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import java.util.List;

@Service
public class TheGenreService implements GenreService {

    private GenreStorage genreStorage;

    @Autowired
    public TheGenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> get() {
        return genreStorage.get();
    }

    public Genre get(Integer id) {
        if (genreStorage.get(id) == null) {
            throw new NotFoundException("Жанр не найден");
        }
        return genreStorage.get(id);
    }
}
