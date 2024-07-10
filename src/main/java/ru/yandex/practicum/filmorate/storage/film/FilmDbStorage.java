package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage implements FilmStorage{

    private JdbcTemplate jdbcTemplate;
    private GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "insert into films(name, description, releaseDate, duration, mpa) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().get("id"));
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        for (Genre g : film.getGenres()) {
            String gQuery = "insert into film_genres(film_id, genre_id) values (?, ?)";
            jdbcTemplate.update(gQuery, film.getId(), g.getId());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String query = "update films set name = ?, description = ?, releaseDate = ?, duration = ?, mpa = ? where film_id = ?";
        jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa(), film.getId());
        return film;
    }

    @Override
    public List<Film> get() {
        String query = "select film_id, name, description, releaseDate, duration, mpa from films";
        return jdbcTemplate.query(query, this::mapRowToFilm);
    }

    @Override
    public Film get(Integer id) {
        String query = "select film_id, name, description, releaseDate, duration, mpa from films where film_id = ?";
        return jdbcTemplate.queryForObject(query, this::mapRowToFilm, id);
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        String query = "insert into likes(film_id, user_id) values (?, ?)";
        jdbcTemplate.update(query, id, userId);
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        String query = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(query, id, userId);
    }

    @Override
    public List<Film> getMostPopular(int count) {
        List<Film> films = get();
        Map<Integer, Set<Integer>> likes = new LinkedHashMap<>();
        String query = "select film_id, user_id from likes";
        for (Map<String, Object> l : jdbcTemplate.queryForList(query)) {
            int id = (int) l.get("FILM_ID");
            int userId = (int) l.get("USER_ID");
            if (!likes.containsKey(id)) {
                likes.put(id, new HashSet<>());
            }
            likes.get(id).add(userId);
        }
        return films.stream().sorted((x1, x2) -> likes.get(x2.getId()).size() - likes.get(x1.getId()).size()).limit(count).collect(Collectors.toList());
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Map<String, Integer> mpa = new HashMap<>();
        mpa.put("id", resultSet.getInt("mpa"));
        Set<Genre> genres = new HashSet<>();
        String query = "select genre_id from film_genres where film_id = ?";
        for (Map<String, Object> g : jdbcTemplate.queryForList(query, resultSet.getInt("film_id"))) {
            genres.add(genreStorage.get((Integer) g.get("genre_id")));
        }
        return new Film(resultSet.getInt("film_id"), resultSet.getString("name"), resultSet.getString("description"),
                resultSet.getDate("releaseDate").toLocalDate(), resultSet.getInt("duration"), mpa, genres);
    }
}
