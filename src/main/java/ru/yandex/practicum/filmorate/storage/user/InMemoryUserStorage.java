package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.*;
import java.util.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private Map<Integer, User> users = new LinkedHashMap<>();
    @Getter
    private Map<Integer, Set<Integer>> friends = new LinkedHashMap<>();
    private int currentId = 1;
    @Autowired
    private ValidateService validator;
    @Autowired
    private UserService service;

    @SneakyThrows
    public User add(User user) {
        validator.checkUser(user);
        user.setId(currentId++);
        users.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>());
        return user;
    }

    @SneakyThrows
    public User update(User user) {
        validator.checkUser(user);
        if (!(users.containsKey(user.getId()))) {
            throw new NotFoundException("Пользователь не найден");
        }
        users.replace(user.getId(), user);
        return user;
    }

    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    public User getUser(Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(id);
    }

    public void addFriends(Integer id, Integer friendId) {
        if (!(users.containsKey(id) && users.containsKey(friendId))) {
            throw new NotFoundException("Пользователь не найден");
        }
        friends.get(id).add(friendId);
        friends.get(friendId).add(id);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        if (!(users.containsKey(id) && users.containsKey(friendId))) {
            throw new NotFoundException("Пользователь не найден");
        }
        friends.get(id).remove(friendId);
        friends.get(friendId).remove(id);
    }

    public List<User> getFriends(Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        List<User> fr = new ArrayList<>();
        for (Integer f : friends.get(id)) {
            fr.add(users.get(f));
        }
        return fr;
    }

    public List<User> getMutuals(Integer id, Integer otherId) {
        if (!(users.containsKey(id) && users.containsKey(otherId))) {
            throw new NotFoundException("Пользователь не найден");
        }
        List<User> fr = new ArrayList<>();
        for (Integer f : service.getMutuals(id, otherId)) {
            fr.add(users.get(f));
        }
        return fr;
    }
}
