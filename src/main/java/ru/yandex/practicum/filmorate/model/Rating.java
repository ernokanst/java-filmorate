package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    private int id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return id == rating.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
