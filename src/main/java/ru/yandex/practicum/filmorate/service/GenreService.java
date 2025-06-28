package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenresMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GenreService {
    private final GenresStorage genresStorage;

    @Autowired
    public GenreService(GenreDbStorage genresStorage) {
        this.genresStorage = genresStorage;
    }

    public List<GenreDto> findAll() {
        return genresStorage.getAllGenres().stream()
                .map(GenresMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

    public GenreDto getGenreById(Long id) {
        Genre genre = genresStorage.getGenreById(id)
                .orElseThrow(() -> {
                    log.error("Genre not found");
                    return new ObjectNotFoundException("Не удалось найти жанр по указанному идентификатору", id);
                });

        return GenresMapper.mapToGenreDto(genre);
    }
}
