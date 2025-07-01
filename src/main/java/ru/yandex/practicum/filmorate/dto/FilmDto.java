package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.deserializer.MinutesToDurationDeserializer;
import ru.yandex.practicum.filmorate.model.serializer.DurationToMinutesSerializer;
import ru.yandex.practicum.filmorate.model.validation.annotation.IsAfter;
import ru.yandex.practicum.filmorate.model.validation.annotation.PositiveDuration;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
public class FilmDto {
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> likes = new HashSet<>();

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @IsAfter(current = "1895-12-27")
    private LocalDate releaseDate;

    @JsonSerialize(using = DurationToMinutesSerializer.class)
    @JsonDeserialize(using = MinutesToDurationDeserializer.class)
    @PositiveDuration
    private Duration duration;

    private Set<Genre> genres = new TreeSet<>();

    private MPA mpa;
}
