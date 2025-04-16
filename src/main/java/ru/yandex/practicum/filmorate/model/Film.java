package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.deserializer.MinutesToDurationDeserializer;
import ru.yandex.practicum.filmorate.model.serializer.DurationToMinutesSerializer;
import ru.yandex.practicum.filmorate.model.validation.annotation.IsAfter;
import ru.yandex.practicum.filmorate.model.validation.annotation.PositiveDuration;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private Long id;

    @NotBlank
    private String name;

    @Pattern(regexp = "^.{1,200}$")
    private String description;

    @IsAfter(current = "1895-12-27")
    private LocalDate releaseDate;

    @JsonSerialize(using = DurationToMinutesSerializer.class)
    @JsonDeserialize(using = MinutesToDurationDeserializer.class)
    @PositiveDuration
    private Duration duration;
}
