package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private int currentID = 1;

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (user.getEmail() != null && user.getEmail().contains("@") && user.getLogin() != null &&
                !(user.getLogin().isBlank()) && !(user.getLogin().contains(" ")) && !(user.getBirthday().isAfter(LocalDate.now()))) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(currentID++);
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Ошибка добавления пользователя");
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
        } else {
            throw new ValidationException("Ошибка обновления пользователя");
        }
        return user;
    }

    @GetMapping
    public List<User> getUser() {
        return new ArrayList<>(users.values());
    }
}
