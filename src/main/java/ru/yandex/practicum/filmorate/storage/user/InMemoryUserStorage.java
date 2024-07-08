package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new LinkedHashMap<>();
    private Map<Integer, Set<Integer>> friends = new LinkedHashMap<>();
    private int currentId = 1;

    public User add(User user) {
        user.setId(currentId++);
        users.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>());
        return user;
    }

    public User update(User user) {
        users.replace(user.getId(), user);
        return user;
    }

    public Map<Integer, User> get() {
        return users;
    }

    public User get(Integer id) {
        return users.get(id);
    }

    public void addFriends(Integer id, Integer friendId) {
        friends.get(id).add(friendId);
        friends.get(friendId).add(id);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        friends.get(id).remove(friendId);
        friends.get(friendId).remove(id);
    }

    public List<User> getFriends(Integer id) {
        List<User> fr = new ArrayList<>();
        for (Integer f : friends.get(id)) {
            fr.add(users.get(f));
        }
        return fr;
    }

    public List<User> getMutuals(Integer id, Integer otherId) {
        List<User> fr = new ArrayList<>();
        for (Integer f : friends.get(id).stream().filter(x -> friends.get(otherId).contains(x)).collect(Collectors.toList())) {
            fr.add(users.get(f));
        }
        return fr;
    }
}
