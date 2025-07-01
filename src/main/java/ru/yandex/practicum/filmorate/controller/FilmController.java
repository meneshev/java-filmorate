package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping(value = "/{id}")
    public FilmDto getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    public List<FilmDto> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody CreateFilmRequest request) {
        return filmService.create(request);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest request) {
        return filmService.update(request);
    }

    @PutMapping(value = "/{filmId}/like/{userId}")
    public FilmDto addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
        return filmService.getFilmById(filmId);
    }

    @DeleteMapping(value = "/{filmId}/like/{userId}")
    public FilmDto deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.deleteLike(filmId, userId);
        return filmService.getFilmById(filmId);
    }

    @GetMapping(value = "/popular")
    public List<FilmDto> getPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }
}
