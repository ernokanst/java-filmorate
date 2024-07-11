package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.*;

@Service
public class TheUserService implements UserService {
    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;
    @Autowired
    ValidateService validateService;

    public User add(User user) {
        validateService.checkUser(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        validateService.checkUser(user);
        try {
            userStorage.get(user.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.update(user);
    }

    public List<User> get() {
        return userStorage.get();
    }

    public User getUser(Integer id) {
        try {
            userStorage.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.get(id);
    }

    public void addFriends(Integer id, Integer friendId) {
        try {
            userStorage.get(id);
            userStorage.get(friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
        userStorage.addFriends(id, friendId);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        try {
            userStorage.get(id);
            userStorage.get(friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
        userStorage.deleteFriends(id, friendId);
    }

    public List<User> getFriends(Integer id) {
        try {
            userStorage.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getFriends(id);
    }

    public List<User> getMutuals(Integer id, Integer otherId) {
        try {
            userStorage.get(id);
            userStorage.get(otherId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getMutuals(id, otherId);
    }
}
