package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAll() {
        return filmStorage.getAllFilms();
    }

    public Optional<Film> getFilmById(Long id) {
        if (filmStorage.getFilmById(id).isEmpty()) {
            log.error("User specified wrong path variable [filmId]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор фильма", id);
        }
        return filmStorage.getFilmById(id);
    }

    public Film create(Film newFilm) {
        if (newFilm.getId() != null) {
            log.error("User specified wrong parameter in request body [film.id]");
            throw new ValidationException("При добавлении нового фильма не нужно указывать идентификатор",
                    newFilm.getId().toString());
        }
        log.info("new film was created: {}", newFilm);
        return filmStorage.create(newFilm);
    }

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("User not specified required parameter in request body [film.id]");
            throw new ValidationException("При изменении фильма необходимо указание идентификатора", null);
        }

        if (filmStorage.getAllFilms().stream().noneMatch(film -> Objects.equals(film.getId(), newFilm.getId()))) {
            log.error("User specified wrong parameter in request body [film.id]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор фильма", newFilm.getId());
        }
        log.info("film was updated: {}", newFilm);
        return filmStorage.update(newFilm);
    }

    public void addLike(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            log.error("User specified wrong path variable [filmId]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор фильма", filmId);
        }

        if (userStorage.getUserById(userId).isEmpty()) {
            log.error("User specified wrong path variable [userId]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", userId);
        }
        log.info("user with id:{} added like to film with id:{}", userId, filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            log.error("User specified wrong path variable [filmId]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор фильма", filmId);
        }

        if (userStorage.getUserById(userId).isEmpty()) {
            log.error("User specified wrong path variable [userId]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", userId);
        }

        if (!filmStorage.getFilmById(filmId).get().getLikes().contains(userId)) {
            log.error("User specified wrong path variable [userId]");
            throw new ValidationException("Данный пользователь не ставил лайк этому фильму", userId.toString());
        }
        log.info("user with id:{} deleted like to film with id:{}", userId, filmId);
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count != null && count < 1) {
            log.error("User specified wrong parameter [count]");
            throw new ValidationException("Параметр count должен быть больше нуля", count.toString());
        }
        return filmStorage.getPopularFilms(count);
    }
}
