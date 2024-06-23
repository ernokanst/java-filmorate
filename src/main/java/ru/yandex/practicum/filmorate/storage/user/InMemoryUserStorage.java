package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.*;
import java.util.*;
import java.util.stream.Collectors;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

@RestController
@RequestMapping("/users")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private Map<Integer, User> users = new LinkedHashMap<>();
    private int currentId = 1;
    @Autowired
    private ValidateService validator;
    @Autowired
    private UserService service;

    @SneakyThrows
    @PostMapping
    public User add(@RequestBody User user) {
        validator.checkUser(user);
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    @SneakyThrows
    @PutMapping
    public User update(@RequestBody User user) {
        validator.checkUser(user);
        if (!(users.containsKey(user.getId()))) {
            throw new NotFoundException("Пользователь не найден");
        }
        users.replace(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (!(users.containsKey(id) && users.containsKey(friendId))) {
            throw new NotFoundException("Пользователь не найден");
        }
        service.addFriends(users.get(id), users.get(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (!(users.containsKey(id) && users.containsKey(friendId))) {
            throw new NotFoundException("Пользователь не найден");
        }
        service.removeFriends(users.get(id), users.get(friendId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        List<User> friends = new ArrayList<>();
        for (Integer f : users.get(id).getFriends()) {
            friends.add(users.get(f));
        }
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutuals(@PathVariable Integer id, @PathVariable Integer otherId) {
        if (!(users.containsKey(id) && users.containsKey(otherId))) {
            throw new NotFoundException("Пользователь не найден");
        }
        List<User> friends = new ArrayList<>();
        for (Integer f : service.getMutuals(users.get(id), users.get(otherId))) {
            friends.add(users.get(f));
        }
        return friends;
    }
}
