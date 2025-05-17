package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> findAll() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> {
                    log.error("Film not found");
                    return new ObjectNotFoundException("Не удалось найти фильм по указанному идентификатору", id);
        });
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

        return filmStorage.getFilmById(newFilm.getId())
                .map(existingFilm -> {
                    log.info("film was updated: {}", newFilm);
                    return filmStorage.update(newFilm);
                })
                .orElseThrow(() -> {
                    log.error("User specified wrong parameter in request body [film.id]");
                    return new ObjectNotFoundException(
                        "Указан несуществующий идентификатор фильма",
                        newFilm.getId());
                });
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [filmId]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор фильма", filmId);
                });

        userStorage.getUserById(userId)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [userId]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", userId);
                });

        log.info("user with id:{} added like to film with id:{}", userId, filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [userId]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", userId);
                });

        filmStorage.getFilmById(filmId)
                .map(film -> {
                    if (!film.getLikes().contains(userId)) {
                        log.error("User specified wrong path variable [userId]");
                        throw new ValidationException(
                                "Данный пользователь не ставил лайк этому фильму",
                                userId.toString()
                        );
                    }
                    return film;
                })
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [filmId]");
                    return new ObjectNotFoundException(
                            "Указан несуществующий идентификатор фильма",
                            filmId);

                });

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
