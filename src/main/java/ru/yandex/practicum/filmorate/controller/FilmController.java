package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> movies = new HashMap<>();

//TODO make logs and tests
    @GetMapping
    public Collection<Film> findAll() {
        log.info("Request GET /films received");
        return movies.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) {
        log.info("Request POST /films received, body:{}", newFilm);
        if (newFilm.getId() != null) {
            log.error("Request POST /films, request body has id");
            throw new ValidationException("При добавлении нового фильма не нужно указывать id");
        }

        newFilm.setId(getNextId());
        movies.put(newFilm.getId(), newFilm);
        log.info("Request POST /films, create new object:{}", newFilm);
        return newFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Request PUT /films received, body:{}", newFilm);
        if (newFilm.getId() == null) {
            log.error("Request PUT /films, request body hasn't id");
            throw new ValidationException("При изменении фильма необходимо указание id");
        }

        if (!movies.containsKey(newFilm.getId())) {
            log.error("Request PUT /films, id {} not found", newFilm.getId());
            throw new ValidationException("Указан несуществующий id");
        }

        movies.put(newFilm.getId(), newFilm);
        log.info("Request PUT /films, updated object:{}", newFilm);
        return newFilm;
    }

    private Long getNextId() {
        return movies.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L) + 1L;
    }
}
