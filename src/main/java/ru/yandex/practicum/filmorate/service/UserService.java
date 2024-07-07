package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import org.springframework.context.ApplicationContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private ApplicationContext context;
    
    public List<Integer> getMutuals(Integer user1, Integer user2) {
        Map<Integer, Set<Integer>> friends = context.getBean(InMemoryUserStorage.class).getFriends();
        return friends.get(user1).stream().filter(x -> friends.get(user2).contains(x)).collect(Collectors.toList());
    }
}
