package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcGenreStorage implements GenreStorage {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcGenreStorage(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> get() {
        String query = "select genre_id, name from genres";
        return jdbcTemplate.query(query, this::mapRowToGenre);
    }

    public Genre get(Integer id) {
        String query = "select genre_id, name from genres where genre_id = :id";
        try {
            return jdbcTemplate.query(query, new MapSqlParameterSource().addValue("id", id), this::mapRowToGenre).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean check(Set<Genre> genres) {
        Set<Integer> receivedIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());
        SqlParameterSource params = new MapSqlParameterSource("ids", receivedIds);
        Set<Genre> inStorage = new HashSet<>(jdbcTemplate.query("SELECT * FROM genres WHERE genre_id IN (:ids)", params, this::mapRowToGenre));
        Set<Integer> actualIds = inStorage.stream().map(Genre::getId).collect(Collectors.toSet());
        return actualIds.containsAll(receivedIds);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"), resultSet.getString("name"));
    }
}
