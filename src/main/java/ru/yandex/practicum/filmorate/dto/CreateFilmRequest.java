package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.validation.annotation.IsAfter;
import ru.yandex.practicum.filmorate.model.validation.annotation.PositiveDuration;
import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class CreateFilmRequest {
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @IsAfter(current = "1895-12-27")
    private LocalDate releaseDate;
    @PositiveDuration
    private Duration duration;
    private MPA mpa;
    private Set<Genre> genres = new LinkedHashSet<>();
}
