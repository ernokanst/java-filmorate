package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserDbStorage implements UserStorage{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "insert into users(email, login, name, birthday) values (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User update(User user) {
        String query = "update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(query, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public List<User> get() {
        String query = "select user_id, email, login, name, birthday from users";
        return jdbcTemplate.query(query, this::mapRowToUser);
    }

    @Override
    public User get(Integer id) {
        String query = "select user_id, email, login, name, birthday from users where user_id = ?";
        return jdbcTemplate.queryForObject(query, this::mapRowToUser, id);
    }

    @Override
    public void addFriends(Integer id, Integer friendId) {
        String query = "insert into friendships(user1, user2) values (?, ?)";
        jdbcTemplate.update(query, id, friendId);
    }

    @Override
    public void deleteFriends(Integer id, Integer friendId) {
        String query = "delete from friendships where user1 = ? and user2 = ?";
        jdbcTemplate.update(query, id, friendId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        String query = "select user1, user2 from friendships where user1 = ?";
        List<List<Integer>> friendships = jdbcTemplate.query(query, this::mapRowToFriendship, id);
        List<User> friends = new ArrayList<>();
        for (List<Integer> f : friendships) {
            friends.add(get(f.get(1)));
        }
        return friends;
    }

    @Override
    public List<User> getMutuals(Integer id, Integer otherId) {
        String query = "select user1, user2 from friendships where user1 = ?";
        List<List<Integer>> friendships1 = jdbcTemplate.query(query, this::mapRowToFriendship, id);
        List<List<Integer>> friendships2 = jdbcTemplate.query(query, this::mapRowToFriendship, otherId);
        List<User> friends = new ArrayList<>();
        for (List<Integer> f : friendships1.stream().filter(x -> friendships2.stream().map(y -> y.get(1)).collect(Collectors.toList()).contains(x.get(1))).collect(Collectors.toList())) {
            friends.add(get(f.get(1)));
        }
        return friends;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt("user_id"), resultSet.getString("email"), resultSet.getString("login"), resultSet.getString("name"), resultSet.getDate("birthday").toLocalDate());
    }

    private List<Integer> mapRowToFriendship (ResultSet resultSet, int rowNum) throws SQLException {
        return List.of(resultSet.getInt("user1"), resultSet.getInt("user2"));
    }
}
