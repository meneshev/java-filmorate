package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping(value = "/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id).get();
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) {
        return filmService.create(newFilm);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping(value = "/{filmId}/like/{userId}")
    public Film addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
        return filmService.getFilmById(filmId).orElseThrow();
    }

    @DeleteMapping(value = "/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.deleteLike(filmId, userId);
        return filmService.getFilmById(filmId).orElseThrow();
    }

    @GetMapping(value = "/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }
}
