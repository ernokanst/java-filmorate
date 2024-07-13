package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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
        String query = "insert into films(name, description, releaseDate, duration, mpa)" +
                " values (:name, :description, :releaseDate, :duration, :mpa)";
        jdbcTemplate.update(query, new MapSqlParameterSource(film.toMap()), generatedKeyHolder);
        film.setId(generatedKeyHolder.getKey().intValue());
        addGenres(film);
        return film;
    }

    private void addGenres(Film film) {
        if (film.getGenres() != null) {
            List<Map<String, Integer>> filmGenres = new ArrayList<>();
            for (Genre g : film.getGenres()) {
                Map<String, Integer> filmGenre = new HashMap<>();
                filmGenre.put("film_id", film.getId());
                filmGenre.put("genre_id", g.getId());
                filmGenres.add(filmGenre);
            }
            SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(filmGenres);
            String gQuery = "insert into film_genres(film_id, genre_id)" +
                    " values (:film_id, :genre_id)";
            jdbcTemplate.batchUpdate(gQuery, batch);
        }
    }

    private Film assembleGenres(List<Film> films) {
        Film film = films.get(0);
        if (!film.getGenres().isEmpty()) {
            film.setGenres(films.stream().map(x -> x.getGenres().stream().findFirst().get()).sorted((x1, x2) -> x1.getId() - x2.getId()).collect(Collectors.toSet()));
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String query = "update films set" +
                " name = :name, description = :description, releaseDate = :releaseDate, duration = :duration, mpa = :mpa" +
                " where film_id = :id";
        jdbcTemplate.update(query, new MapSqlParameterSource(film.toMap()));
        query = "delete from film_genres where film_id = :id";
        jdbcTemplate.update(query, new BeanPropertySqlParameterSource(film));
        addGenres(film);
        return film;
    }

    @Override
    public List<Film> get() {
        String query = "select f.*, m.name as mpa_name, fg.genre_id, g.name as genre_name from films as f " +
                "left join mpa as m on f.mpa = m.mpa_id " +
                "left join film_genres as fg on f.film_id = fg.film_id " +
                "left join genres as g on fg.genre_id = g.genre_id";
        List<Film> received = jdbcTemplate.query(query, this::mapRowToFilm);
        List<Film> assembled = new ArrayList<>();
        for (List<Film> lf : received.stream().collect(groupingBy(Film::getId)).values()) {
            assembled.add(assembleGenres(lf));
        }
        return assembled;
    }

    @Override
    public Film get(Integer id) {
        String query = "select f.*, m.name as mpa_name, fg.genre_id, g.name as genre_name from films as f " +
                "left join mpa as m on f.mpa = m.mpa_id " +
                "left join film_genres as fg on f.film_id = fg.film_id " +
                "left join genres as g on fg.genre_id = g.genre_id " +
                "where f.film_id = :id";
        List<Film> received = jdbcTemplate.query(query, new MapSqlParameterSource().addValue("id", id), this::mapRowToFilm);
        return assembleGenres(received);
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("film_id", id).addValue("user_id", userId);
        String query = "merge into likes using values (:film_id, :user_id) as new_like(film_id, user_id) " +
                "on likes.film_id = new_like.film_id and likes.user_id = new_like.user_id " +
                "when not matched then insert (film_id, user_id) values (new_like.film_id, new_like.user_id)";
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
        String query = "select f.*, m.name as mpa_name, fg.genre_id, g.name as genre_name from films as f " +
                "left join mpa as m on f.mpa = m.mpa_id " +
                "left join film_genres as fg on f.film_id = fg.film_id " +
                "left join genres as g on fg.genre_id = g.genre_id " +
                "ORDER BY (select count(l.film_id) from likes as l where l.film_id = f.film_id) DESC";
        List<Film> received = jdbcTemplate.query(query, this::mapRowToFilm);
        Map<Integer, List<Film>> counted = received.stream().collect(groupingBy(Film::getId));
        List<Film> assembled = new ArrayList<>();
        for (Film f : received) {
            if (!assembled.stream().map(Film::getId).collect(Collectors.toSet()).contains(f.getId())) {
                if (counted.get(f.getId()).size() > 1) {
                    assembled.add(assembleGenres(counted.get(f.getId())));
                } else {
                    assembled.add(f);
                }
            }
        }
        return assembled;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Set<Genre> genres = new HashSet<>();
        Genre genre = new Genre(resultSet.getInt("genre_id"), resultSet.getString("genre_name"));
        if (genre.getId() != 0) {
            genres.add(genre);
        }
        Rating mpa = new Rating(resultSet.getInt("mpa"), resultSet.getString("mpa_name"));
        return new Film(resultSet.getInt("film_id"), resultSet.getString("name"), resultSet.getString("description"),
                resultSet.getDate("releaseDate").toLocalDate(), resultSet.getInt("duration"), genres, mpa);
    }
}
