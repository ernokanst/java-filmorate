package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> users = new LinkedHashMap<>();
    private int currentId = 1;

    @PostMapping
    public User add(@RequestBody User user) {
        if (ValidateService.checkUser(user)) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(currentId++);
            users.put(user.getId(), user);
            return user;
        }
        throw new ValidationException("Ошибка добавления пользователя");
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (ValidateService.checkUser(user)) {
            if (users.containsKey(user.getId())) {
                users.replace(user.getId(), user);
                return user;
            }
        }
        throw new ValidationException("Ошибка обновления пользователя");
    }

    @GetMapping
    public List<User> get() {
        return new ArrayList<>(users.values());
    }
}
