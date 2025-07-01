package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateUserRequest {
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
