package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;
import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenresController {
    private final GenreService genresService;

    @GetMapping(value = "/{id}")
    public GenreDto getGenreById(@PathVariable Long id) {
        return genresService.getGenreById(id);
    }

    @GetMapping
    public List<GenreDto> findAll() {
        return genresService.findAll();
    }
}
