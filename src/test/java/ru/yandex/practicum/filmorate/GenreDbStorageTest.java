package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({GenreDbStorage.class, GenreRowMapper.class})
class GenreDbStorageTest {
    @Autowired
    private GenreDbStorage genreDbStorage;

    @Test
    void testGetGenreById() {
        Optional<Genre> foundGenre = genreDbStorage.getGenreById(1L);

        assertThat(foundGenre)
                .isPresent()
                .hasValueSatisfying(genre -> {
                    assertThat(genre.getId()).isEqualTo(1L);
                    assertThat(genre.getName()).isEqualTo("Комедия");
                });
    }

    @Test
    void shouldGetAllGenres() {
        List<Genre> genres = genreDbStorage.getAllGenres();

        assertThat(genres)
                .extracting(Genre::getName)
                .contains("Комедия", "Драма");
    }
}
