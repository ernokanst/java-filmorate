package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    public void addFriends(User user1, User user2) {
        user1.addFriend(user2.getId());
        user2.addFriend(user1.getId());
    }

    public void removeFriends(User user1, User user2) {
        user1.removeFriend(user2.getId());
        user2.removeFriend(user1.getId());
    }

    public List<Integer> getMutuals(User user1, User user2) {
        return user1.getFriends().stream().filter(x -> user2.getFriends().contains(x)).collect(Collectors.toList());
    }
}
