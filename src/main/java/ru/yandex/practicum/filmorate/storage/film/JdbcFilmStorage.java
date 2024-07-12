package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Primary
public class JdbcFilmStorage implements FilmStorage {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcFilmStorage(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        String query = "insert into films(name, description, releaseDate, duration, mpa) values (:name, :description, :releaseDate, :duration, :mpa)";
        jdbcTemplate.update(query, new MapSqlParameterSource(film.toMap()), generatedKeyHolder);
        film.setId(generatedKeyHolder.getKey().intValue());
        if (film.getGenres() != null) {
            addGenres(film);
        }
        return film;
    }

    private void addGenres(Film film) {
        List<Map<String, Integer>> filmGenres = new ArrayList<>();
        for (Genre g : film.getGenres()) {
            Map<String, Integer> filmGenre = new HashMap<>();
            filmGenre.put("film_id", film.getId());
            filmGenre.put("genre_id", g.getId());
            filmGenres.add(filmGenre);
        }
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(filmGenres);
        String gQuery = "insert into film_genres(film_id, genre_id) values (:film_id, :genre_id)";
        jdbcTemplate.batchUpdate(gQuery, batch);
    }

    @Override
    public Film update(Film film) {
        String query = "update films set name = :name, description = :description, releaseDate = :releaseDate, duration = :duration, mpa = :mpa where film_id = :id";
        jdbcTemplate.update(query, new MapSqlParameterSource(film.toMap()));
        if (film.getGenres() != null) {
            query = "delete from film_genres where film_id = :id";
            jdbcTemplate.update(query, new BeanPropertySqlParameterSource(film));
            addGenres(film);
        }
        return film;
    }

    @Override
    public List<Film> get() {
        String query = "select f.*, m.name as mpa_name from films as f left outer join mpa as m on f.mpa = m.mpa_id";
        return jdbcTemplate.query(query, this::mapRowToFilm);
    }

    @Override
    public Film get(Integer id) {
        String query = "select f.*, m.name as mpa_name from films as f left outer join mpa as m on f.mpa = m.mpa_id where f.film_id = :id";
        return jdbcTemplate.queryForObject(query, new MapSqlParameterSource().addValue("id", id), this::mapRowToFilm);
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("film_id", id).addValue("user_id", userId);
        String query = "insert into likes(film_id, user_id) values (:film_id, :user_id)";
        jdbcTemplate.update(query, params);
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("film_id", id).addValue("user_id", userId);
        String query = "delete from likes where film_id = :film_id and user_id = :user_id";
        jdbcTemplate.update(query, params);
    }

    @Override
    public List<Film> getMostPopular(int count) {
        String query = "select f.*, m.name as mpa_name from films as f left outer join mpa as m on f.mpa = m.mpa_id ORDER BY (select count(l.film_id) from likes as l where l.film_id = f.film_id) DESC";
        return jdbcTemplate.query(query, new MapSqlParameterSource().addValue("limit", count), this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Set<Genre> genres = new HashSet<>();
        String query = "select genre_id from film_genres where film_id = :id";
        for (Map<String, Object> g : jdbcTemplate.queryForList(query, new MapSqlParameterSource().addValue("id", resultSet.getInt("film_id")))) {
            Genre genre = jdbcTemplate.queryForObject("select genre_id, name from genres where genre_id = :id", new MapSqlParameterSource().addValue("id", (Integer) g.get("genre_id")), (ResultSet rs, int rn) -> new Genre(rs.getInt("genre_id"), rs.getString("name")));
            genres.add(genre);
        }
        Rating mpa = new Rating(resultSet.getInt("mpa"), resultSet.getString("mpa_name"));
        return new Film(resultSet.getInt("film_id"), resultSet.getString("name"), resultSet.getString("description"),
                resultSet.getDate("releaseDate").toLocalDate(), resultSet.getInt("duration"), genres, mpa);
    }
}
