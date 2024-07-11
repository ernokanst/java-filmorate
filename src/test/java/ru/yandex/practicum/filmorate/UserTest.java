package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class})
public class UserTest {

    private final UserDbStorage users;

    @Test
    public void testAdd() {
        User user = new User("email@email.com", "user", "John Doe", LocalDate.of(2000, 1, 1));
        User addedUser = users.add(user);
        assertEquals(1, addedUser.getId());
    }

    @Test
    public void testUpdate() {
        User user = new User("email@email.com", "user", "John Doe", LocalDate.of(2000, 1, 1));
        user = users.add(user);
        user.setEmail("diffirent_email@email.com");
        user.setLogin("login");
        user.setName("Jane Doe");
        user.setBirthday(LocalDate.of(1987, 6, 5));
        User addedUser = users.update(user);
        assertEquals(user, addedUser);
    }

    @Test
    public void testGetAll() {
        User user1 = new User("email@email.com", "user", "John Doe", LocalDate.of(2000, 1, 1));
        User user2 = new User("diffirent_email@email.com", "login", "Jane Doe", LocalDate.of(1987, 6, 5));
        User user3 = new User("test@email.com", "test", "Test User", LocalDate.of(2002, 2, 22));
        users.add(user1);
        users.add(user2);
        users.add(user3);
        List<User> getUsers = users.get();
        assertEquals(getUsers, List.of(user1, user2, user3));
    }

    @Test
    public void testGetOne() {
        User user1 = new User("email@email.com", "user", "John Doe", LocalDate.of(2000, 1, 1));
        User user2 = new User("diffirent_email@email.com", "login", "Jane Doe", LocalDate.of(1987, 6, 5));
        User user3 = new User("test@email.com", "test", "Test User", LocalDate.of(2002, 2, 22));
        users.add(user1);
        user2.setId(users.add(user2).getId());
        users.add(user3);
        User getUser = users.get(user2.getId());
        assertEquals(getUser, user2);
    }

    @Test
    public void testAddFriends() {
        User user1 = new User("email@email.com", "user", "John Doe", LocalDate.of(2000, 1, 1));
        User user2 = new User("diffirent_email@email.com", "login", "Jane Doe", LocalDate.of(1987, 6, 5));
        user1.setId(users.add(user1).getId());
        user2.setId(users.add(user2).getId());
        List<User> getFriends = users.getFriends(user1.getId());
        assertEquals(getFriends, new ArrayList<>());
        getFriends = users.getFriends(user2.getId());
        assertEquals(getFriends, new ArrayList<>());
        users.addFriends(user1.getId(), user2.getId());
        getFriends = users.getFriends(user1.getId());
        assertEquals(getFriends, List.of(user2));
        getFriends = users.getFriends(user2.getId());
        assertEquals(getFriends, new ArrayList<>());
        users.addFriends(user2.getId(), user1.getId());
        getFriends = users.getFriends(user1.getId());
        assertEquals(getFriends, List.of(user2));
        getFriends = users.getFriends(user2.getId());
        assertEquals(getFriends, List.of(user1));
    }

    @Test
    public void testDeleteFriends() {
        User user1 = new User("email@email.com", "user", "John Doe", LocalDate.of(2000, 1, 1));
        User user2 = new User("diffirent_email@email.com", "login", "Jane Doe", LocalDate.of(1987, 6, 5));
        user1.setId(users.add(user1).getId());
        user2.setId(users.add(user2).getId());
        users.addFriends(user1.getId(), user2.getId());
        users.addFriends(user2.getId(), user1.getId());
        List<User> getFriends = users.getFriends(user1.getId());
        assertEquals(getFriends, List.of(user2));
        getFriends = users.getFriends(user2.getId());
        assertEquals(getFriends, List.of(user1));
        users.deleteFriends(user1.getId(), user2.getId());
        getFriends = users.getFriends(user1.getId());
        assertEquals(getFriends, new ArrayList<>());
        getFriends = users.getFriends(user2.getId());
        assertEquals(getFriends, List.of(user1));
        users.deleteFriends(user2.getId(), user1.getId());
        getFriends = users.getFriends(user1.getId());
        assertEquals(getFriends, new ArrayList<>());
        getFriends = users.getFriends(user2.getId());
        assertEquals(getFriends, new ArrayList<>());
    }

    @Test
    public void testGetFriends() {
        User user1 = new User("email@email.com", "user", "John Doe", LocalDate.of(2000, 1, 1));
        User user2 = new User("diffirent_email@email.com", "login", "Jane Doe", LocalDate.of(1987, 6, 5));
        user1.setId(users.add(user1).getId());
        user2.setId(users.add(user2).getId());
        users.addFriends(user1.getId(), user2.getId());
        users.addFriends(user2.getId(), user1.getId());
        List<User> getFriends = users.getFriends(user1.getId());
        assertEquals(getFriends, List.of(user2));
        getFriends = users.getFriends(user2.getId());
        assertEquals(getFriends, List.of(user1));
    }

    @Test
    public void testGetMutuals() {
        User user1 = new User("email@email.com", "user", "John Doe", LocalDate.of(2000, 1, 1));
        User user2 = new User("diffirent_email@email.com", "login", "Jane Doe", LocalDate.of(1987, 6, 5));
        User user3 = new User("test@email.com", "test", "Test User", LocalDate.of(2002, 2, 22));
        user1.setId(users.add(user1).getId());
        user2.setId(users.add(user2).getId());
        user3.setId(users.add(user3).getId());
        assertEquals(users.getMutuals(user1.getId(), user2.getId()), new ArrayList<>());
        users.addFriends(user1.getId(), user3.getId());
        users.addFriends(user2.getId(), user3.getId());
        users.addFriends(user3.getId(), user1.getId());
        users.addFriends(user3.getId(), user2.getId());
        assertEquals(users.getMutuals(user1.getId(), user2.getId()), List.of(user3));
    }
}
