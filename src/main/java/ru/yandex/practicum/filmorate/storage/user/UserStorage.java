package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {
    User add(User user);

    User update(User user);

    List<User> get();

    User get(Integer id);

    void addFriends(Integer id, Integer friendId);

    void deleteFriends(Integer id, Integer friendId);

    List<User> getFriends(Integer id);

    List<User> getMutuals(Integer id, Integer otherId);
}
