package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.comparator.FilmLikesComparator;
import java.util.*;

@Component
@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> movies = new HashMap<>();

    @Override
    public Film create(Film newFilm) {
        newFilm.setId(getNextId());
        movies.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film update(Film newFilm) {
        movies.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public void delete(Long id) {
        movies.remove(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return movies.values().stream().toList();
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.ofNullable(movies.get(id));
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        movies.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        movies.get(filmId).getLikes().remove(userId);
    }

    @Override
    public Integer getLikesCount(Long filmId) {
        return movies.get(filmId).getLikes().size();
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        if (count != null) {
            return movies.values().stream()
                    .sorted(new FilmLikesComparator().reversed())
                    .limit(count)
                    .toList();
        } else {
            return movies.values().stream()
                    .sorted(new FilmLikesComparator().reversed())
                    .limit(10)
                    .toList();
        }
    }

    private Long getNextId() {
        return movies.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L) + 1L;
    }
}
