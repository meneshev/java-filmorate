package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException {
    private String wrongValue;
    public ValidationException(String message, String value) {
        super(message);
        this.wrongValue = value;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}
