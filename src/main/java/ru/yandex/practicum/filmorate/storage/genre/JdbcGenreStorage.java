package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
            return jdbcTemplate.queryForObject(query, new MapSqlParameterSource().addValue("id", id), this::mapRowToGenre);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"), resultSet.getString("name"));
    }
}
