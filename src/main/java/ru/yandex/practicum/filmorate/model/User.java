package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;

/**
 * User.
 */
@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthday;
}
