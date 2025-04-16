package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class User {
    private Long id;

    @NotEmpty
    @Email
    private String email;

    @Pattern(regexp = "^\\w+$")
    @NotEmpty
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
