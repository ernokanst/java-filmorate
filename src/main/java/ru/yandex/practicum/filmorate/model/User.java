package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * User.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
