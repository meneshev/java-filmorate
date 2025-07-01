package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {
    public static Film mapToFilm(CreateFilmRequest createFilmRequest) {
        Film film = new Film();
        film.setName(createFilmRequest.getName());
        film.setDescription(createFilmRequest.getDescription());
        film.setReleaseDate(createFilmRequest.getReleaseDate());
        film.setDuration(createFilmRequest.getDuration());

        if (createFilmRequest.getMpa().getId() != null) {
            MPA mpa = new MPA();
            mpa.setId(createFilmRequest.getMpa().getId());
            film.setMpa(mpa);
        }

        if (!createFilmRequest.getGenres().isEmpty()) {
            Set<Genre> genres = createFilmRequest.getGenres().stream()
                    .map(genreElement -> {
                        Genre genre = new Genre();
                        genre.setId(genreElement.getId());
                        return genre;
                    })
                    .collect(Collectors.toSet());
            film.setGenres(genres);
        } else {
            film.setGenres(new HashSet<>());
        }
        return film;
    }

    public static Film mapToFilm(UpdateFilmRequest updateFilmRequest) {
        Film film = new Film();
        film.setId(updateFilmRequest.getId());
        film.setName(updateFilmRequest.getName());
        film.setDescription(updateFilmRequest.getDescription());
        film.setReleaseDate(updateFilmRequest.getReleaseDate());
        film.setDuration(updateFilmRequest.getDuration());

        if (updateFilmRequest.getMpa().getId() != null) {
            MPA mpa = new MPA();
            mpa.setId(updateFilmRequest.getMpa().getId());
            film.setMpa(mpa);
        }

        if (!updateFilmRequest.getGenres().isEmpty()) {
            Set<Genre> genres = updateFilmRequest.getGenres().stream()
                    .map(genreElement -> {
                        Genre genre = new Genre();
                        genre.setId(genreElement.getId());
                        return genre;
                    })
                    .collect(Collectors.toSet());
            film.setGenres(genres);
        } else {
            film.setGenres(new HashSet<>());
        }

        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setMpa(film.getMpa());
        filmDto.setDuration(film.getDuration());
        filmDto.setGenres(film.getGenres());
        filmDto.setLikes(film.getLikes());
        return filmDto;
    }

    public static Film updateFilm(Film film, CreateFilmRequest request) {
        if (request.getName() != null) {
            film.setName(request.getName());
        }
        if (request.getDescription() != null) {
            film.setDescription(request.getDescription());
        }
        if (request.getReleaseDate() != null) {
            film.setReleaseDate(request.getReleaseDate());
        }
        if (request.getMpa().getId() != null) {
            MPA mpa = new MPA();
            mpa.setId(request.getMpa().getId());
            film.setMpa(mpa);
        }
        if (request.getDuration() != null) {
            film.setDuration(request.getDuration());
        }
        if (request.getGenres() != null) {
            Set<Genre> genres = request.getGenres().stream()
                    .map(genreElement -> {
                        Genre genre = new Genre();
                        genre.setId(genreElement.getId());
                        return genre;
                    })
                    .collect(Collectors.toSet());
            film.setGenres(genres);
        }
        return film;
    }
}
