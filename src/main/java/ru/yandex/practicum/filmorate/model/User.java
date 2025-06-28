package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Map<Long, Boolean> friends = new HashMap<>();

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
