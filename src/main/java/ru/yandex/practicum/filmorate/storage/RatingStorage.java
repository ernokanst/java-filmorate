package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RatingStorage {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Rating> get() {
        String query = "select mpa_id, name from mpa";
        return jdbcTemplate.query(query, this::mapRowToRating);
    }

    public Rating get(Integer id) {
        String query = "select mpa_id, name from mpa where mpa_id = ?";
        return jdbcTemplate.queryForObject(query, this::mapRowToRating, id);
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return new Rating(resultSet.getInt("mpa_id"), resultSet.getString("name"));
    }
}
