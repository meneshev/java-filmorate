package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreService genreService;
    private final MPAService mpaService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       GenreService genreService, MPAService mpaService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

    public List<FilmDto> findAll() {
        return filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> {
                    log.error("Film not found");
                    return new ObjectNotFoundException("Не удалось найти фильм по указанному идентификатору", id);
        });

        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto create(CreateFilmRequest request) {
        request.getGenres().forEach(genre -> {
            genreService.getGenreById(genre.getId());
        });

        mpaService.getMPAById(request.getMpa().getId());

        Film film = FilmMapper.mapToFilm(request);
        film = filmStorage.create(film);
        log.info("new film was created: {}", film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(UpdateFilmRequest request) {
        if (request.getId() == null) {
            log.error("User not specified required parameter in request body [film.id]");
            throw new ValidationException("При изменении фильма необходимо указание идентификатора", null);
        }
        request.getGenres().forEach(genre -> {
            genreService.getGenreById(genre.getId());
        });

        mpaService.getMPAById(request.getMpa().getId());

        Film film = FilmMapper.mapToFilm(request);

        return FilmMapper.mapToFilmDto(filmStorage.getFilmById(film.getId())
                .map(existingFilm -> {
                    log.info("film was updated: {}", request);
                    return filmStorage.update(film);
                })
                .orElseThrow(() -> {
                    log.error("User specified wrong parameter in request body [film.id]");
                    return new ObjectNotFoundException(
                        "Указан несуществующий идентификатор фильма",
                            film.getId());
                }));
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

    public List<FilmDto> getPopularFilms(Integer count) {
        if (count != null && count < 1) {
            log.error("User specified wrong parameter [count]");
            throw new ValidationException("Параметр count должен быть больше нуля", count.toString());
        }
        return filmStorage.getPopularFilms(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }
}
