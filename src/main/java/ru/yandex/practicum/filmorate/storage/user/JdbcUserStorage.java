package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary
public class JdbcUserStorage implements UserStorage {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserStorage(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        String query = "insert into users(email, login, name, birthday) values (:email, :login, :name, :birthday)";
        jdbcTemplate.update(query, new BeanPropertySqlParameterSource(user), generatedKeyHolder);
        user.setId(generatedKeyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User update(User user) {
        String query = "update users set email = :email, login = :login, name = :name, birthday = :birthday where user_id = :id";
        jdbcTemplate.update(query, new BeanPropertySqlParameterSource(user));
        return user;
    }

    @Override
    public List<User> get() {
        String query = "select user_id, email, login, name, birthday from users";
        return jdbcTemplate.query(query, this::mapRowToUser);
    }

    @Override
    public User get(Integer id) {
        String query = "select user_id, email, login, name, birthday from users where user_id = :id";
        try {
            return jdbcTemplate.queryForObject(query, new MapSqlParameterSource().addValue("id", id), this::mapRowToUser);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void addFriends(Integer id, Integer friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("user1", id).addValue("user2", friendId);
        String query = "insert into friendships(user1, user2) values (:user1, :user2)";
        jdbcTemplate.update(query, params);
    }

    @Override
    public void deleteFriends(Integer id, Integer friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("user1", id).addValue("user2", friendId);
        String query = "delete from friendships where user1 = :user1 and user2 = :user2";
        jdbcTemplate.update(query, params);
    }

    @Override
    public List<User> getFriends(Integer id) {
        String query = "select user1, user2 from friendships where user1 = :id";
        List<List<Integer>> friendships = jdbcTemplate.query(query, new MapSqlParameterSource().addValue("id", id), this::mapRowToFriendship);
        List<User> friends = new ArrayList<>();
        for (List<Integer> f : friendships) {
            friends.add(get(f.get(1)));
        }
        return friends;
    }

    @Override
    public List<User> getMutuals(Integer id, Integer otherId) {
        String query = "select user1, user2 from friendships where user1 = :id";
        List<List<Integer>> friendships1 = jdbcTemplate.query(query, new MapSqlParameterSource().addValue("id", id), this::mapRowToFriendship);
        List<List<Integer>> friendships2 = jdbcTemplate.query(query, new MapSqlParameterSource().addValue("id", otherId), this::mapRowToFriendship);
        List<User> friends = new ArrayList<>();
        for (List<Integer> f : friendships1.stream().filter(x -> friendships2.stream().map(y -> y.get(1)).collect(Collectors.toList()).contains(x.get(1))).collect(Collectors.toList())) {
            friends.add(get(f.get(1)));
        }
        return friends;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt("user_id"), resultSet.getString("email"), resultSet.getString("login"), resultSet.getString("name"), resultSet.getDate("birthday").toLocalDate());
    }

    private List<Integer> mapRowToFriendship(ResultSet resultSet, int rowNum) throws SQLException {
        return List.of(resultSet.getInt("user1"), resultSet.getInt("user2"));
    }
}
