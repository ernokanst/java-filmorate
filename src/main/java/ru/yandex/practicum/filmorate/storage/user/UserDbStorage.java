package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

@Repository
public class UserDbStorage implements UserStorage{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public List<User> get() {
        return List.of();
    }

    @Override
    public User get(Integer id) {
        return null;
    }

    @Override
    public void addFriends(Integer id, Integer friendId) {

    }

    @Override
    public void deleteFriends(Integer id, Integer friendId) {

    }

    @Override
    public List<User> getFriends(Integer id) {
        return List.of();
    }

    @Override
    public List<User> getMutuals(Integer id, Integer otherId) {
        return List.of();
    }
}
