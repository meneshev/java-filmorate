package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.MPARowMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class, FilmRowMapper.class,
        MPARowMapper.class, GenreRowMapper.class})
class FilmDBStorageTest {
    @Autowired
    private FilmDbStorage filmDbStorage;
    private Film testFilm;
    private Film anotherFilm;
    private MPA mpa;
    private Genre genre;

    @BeforeEach
    void setUp() {
        mpa = new MPA();
        mpa.setId(1L);

        genre = new Genre();
        genre.setId(1L);

        testFilm = new Film();
        testFilm.setName("Test Film");
        testFilm.setDescription("Test Description");
        testFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        testFilm.setDuration(Duration.ofMinutes(120));
        testFilm.setMpa(mpa);
        testFilm.setGenres(Set.of(genre));

        anotherFilm = new Film();
        anotherFilm.setName("Another Film");
        anotherFilm.setDescription("Another Description");
        anotherFilm.setReleaseDate(LocalDate.of(2010, 1, 1));
        anotherFilm.setDuration(Duration.ofMinutes(90));
        anotherFilm.setMpa(mpa);
    }

    @Test
    void testCreateAndGetFilmById() {
        Film createdFilm = filmDbStorage.create(testFilm);

        Optional<Film> foundFilm = filmDbStorage.getFilmById(createdFilm.getId());

        assertThat(foundFilm)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getId()).isEqualTo(createdFilm.getId());
                    assertThat(film.getName()).isEqualTo(testFilm.getName());
                    assertThat(film.getMpa().getId()).isEqualTo(mpa.getId());
                    assertThat(film.getGenres()).hasSize(1);
                });
    }

    @Test
    void testGetAllFilms() {
        Film film1 = filmDbStorage.create(testFilm);
        Film film2 = filmDbStorage.create(anotherFilm);

        List<Film> films = filmDbStorage.getAllFilms();

        assertThat(films)
                .extracting(Film::getId)
                .contains(film1.getId(), film2.getId());
    }

    @Test
    void testUpdateFilm() {
        Film createdFilm = filmDbStorage.create(testFilm);

        createdFilm.setName("Updated Film");
        createdFilm.setDescription("Updated Description");

        filmDbStorage.update(createdFilm);

        Optional<Film> foundFilm = filmDbStorage.getFilmById(createdFilm.getId());
        assertThat(foundFilm)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getName()).isEqualTo("Updated Film");
                    assertThat(film.getDescription()).isEqualTo("Updated Description");
                });
    }

    @Test
    void testAddAndRemoveLike() {
        Film film = filmDbStorage.create(testFilm);

        filmDbStorage.addLike(film.getId(), 1L);
        assertThat(filmDbStorage.getLikesCount(film.getId())).isEqualTo(1);

        filmDbStorage.deleteLike(film.getId(), 1L);
        assertThat(filmDbStorage.getLikesCount(film.getId())).isEqualTo(0);
    }

    @Test
    void testGetPopularFilms() {
        List<Film> popularFilms = filmDbStorage.getPopularFilms(null);
        List<Film> popularFilmsLimited = filmDbStorage.getPopularFilms(1);

        assertThat(popularFilms).isNotEmpty();

        assertThat(popularFilmsLimited)
                .hasSize(1);
    }
}
