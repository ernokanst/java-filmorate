package ru.yandex.practicum.filmorate.exceptions;
import org.apache.coyote.BadRequestException;

public class ValidationException extends BadRequestException {
    public ValidationException(String message) {
        super(message);
    }
}
