package ru.yandex.practicum.filmorate.storage.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcRatingStorage implements RatingStorage {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcRatingStorage(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Rating> get() {
        String query = "select mpa_id, name from mpa";
        return jdbcTemplate.query(query, this::mapRowToRating);
    }

    public Rating get(Integer id) {
        String query = "select mpa_id, name from mpa where mpa_id = :id";
        try {
            return jdbcTemplate.queryForObject(query, new MapSqlParameterSource().addValue("id", id), this::mapRowToRating);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return new Rating(resultSet.getInt("mpa_id"), resultSet.getString("name"));
    }
}
