package ru.yandex.practicum.filmorate.exception;

public class ObjectNotFoundException extends RuntimeException {
    private Long id;

    public ObjectNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
