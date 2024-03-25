package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.ValidateService;
import javax.servlet.http.HttpServletResponse;
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
        if (validator.checkUser(user)) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(currentId++);
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user, HttpServletResponse response) {
        if (validator.checkUser(user)) {
            if (users.containsKey(user.getId())) {
                users.replace(user.getId(), user);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        return user;
    }

    @GetMapping
    public List<User> get() {
        return new ArrayList<>(users.values());
    }
}
