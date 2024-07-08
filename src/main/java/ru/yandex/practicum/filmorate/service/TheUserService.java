package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.*;

@Service
public class TheUserService implements UserService {
    @Autowired
    private UserStorage users;
    @Autowired
    ValidateService validator;

    public User add(User user) {
        validator.checkUser(user);
        return users.add(user);
    }

    public User update(User user) {
        validator.checkUser(user);
        if (!(users.get().containsKey(user.getId()))) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.update(user);
    }

    public List<User> get() {
        return new ArrayList<>(users.get().values());
    }

    public User getUser(Integer id) {
        if (!users.get().containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(id);
    }

    public void addFriends(Integer id, Integer friendId) {
        if (!(users.get().containsKey(id) && users.get().containsKey(friendId))) {
            throw new NotFoundException("Пользователь не найден");
        }
        users.addFriends(id, friendId);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        if (!(users.get().containsKey(id) && users.get().containsKey(friendId))) {
            throw new NotFoundException("Пользователь не найден");
        }
        users.deleteFriends(id, friendId);
    }

    public List<User> getFriends(Integer id) {
        if (!users.get().containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.getFriends(id);
    }

    public List<User> getMutuals(Integer id, Integer otherId) {
        if (!(users.get().containsKey(id) && users.get().containsKey(otherId))) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.getMutuals(id, otherId);
    }
}
