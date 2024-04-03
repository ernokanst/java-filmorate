package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.ValidateService;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> users = new LinkedHashMap<>();
    private int currentId = 1;
    private ValidateService validator;

    @Autowired
    public UserController(ValidateService validateService) {
        validator = validateService;
    }

    @PostMapping
    public User add(@RequestBody User user) {
        validator.checkUser(user);
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

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
}
