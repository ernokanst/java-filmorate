package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private InMemoryUserStorage users;

    @SneakyThrows
    @PostMapping
    public User add(@RequestBody User user) {
        users.add(user);
        return user;
    }

    @SneakyThrows
    @PutMapping
    public User update(@RequestBody User user) {
        users.update(user);
        return user;
    }

    @GetMapping
    public List<User> get() {
        return users.get();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return users.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        users.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        users.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        return users.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutuals(@PathVariable Integer id, @PathVariable Integer otherId) {
        return users.getMutuals(id, otherId);
    }
}
