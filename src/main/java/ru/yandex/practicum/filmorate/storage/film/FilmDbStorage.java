package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage  {
    private static final String FIND_FILM_BY_ID_SQL = """
            SELECT
                f.FILM_ID,
                f.FILM_NAME,
                f.FILM_DESCRIPTION,
                f.FILM_RELEASEDATE,
                f.FILM_DURATION,
                f.RATING_ID,
                m.MPA_ID,
                m.MPA_NAME
            FROM FILM f
            LEFT JOIN MPA m ON m.MPA_ID = f.RATING_ID
            WHERE f.FILM_ID = ?
            """;

    private static final String FIND_ALL_FILMS_SQL = """
            SELECT
                f.FILM_ID,
                f.FILM_NAME,
                f.FILM_DESCRIPTION,
                f.FILM_RELEASEDATE,
                f.FILM_DURATION,
                f.RATING_ID,
                m.MPA_ID,
                m.MPA_NAME
            FROM FILM f
            LEFT JOIN MPA m ON m.MPA_ID = f.RATING_ID
            """;

    private static final String CREATE_FILM_SQL = """
            INSERT INTO PUBLIC.FILM
            (FILM_NAME, FILM_DESCRIPTION, FILM_RELEASEDATE, FILM_DURATION, RATING_ID)
            VALUES
            (?, ?, ?, ?, ?)
            """;

    private static final String ADD_GENRE_TO_FILM_SQL = """
            INSERT INTO PUBLIC.FILM_GENRES
            (FILM_ID, GENRE_ID)
            VALUES
            (?, ?)
            """;

    private static final String UPDATE_FILM_SQL = """
            UPDATE PUBLIC.FILM
            SET FILM_NAME=?,
            FILM_DESCRIPTION=?,
            FILM_RELEASEDATE=?,
            FILM_DURATION=?,
            RATING_ID=?
            WHERE FILM_ID=?;
            """;

    private static final String DELETE_FILM_GENRES_SQL = """
            DELETE FROM FILM_GENRES
            WHERE FILM_ID = ?
            """;

    private static final String DELETE_FILM_SQL = """
            DELETE FROM FILM
            WHERE FILM_ID = ?
            """;

    private static final String ADD_FILM_LIKE_SQL = """
            INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID)
            SELECT t.* FROM (VALUES (?, ?)) AS t(FILM_ID, USER_ID)
            WHERE NOT EXISTS (
              SELECT 1 FROM PUBLIC.FILM_LIKES
              WHERE FILM_ID = t.FILM_ID
              AND USER_ID = t.USER_ID)
            """;

    private static final String DELETE_FILM_LIKE_SQL = """
            DELETE FROM FILM_LIKES
            WHERE FILM_ID = ? AND USER_ID = ?
            """;

    private static final String GET_FILM_LIKES_SQL = """
            SELECT COUNT(fl.*)
            FROM FILM_LIKES fl
            WHERE fl.FILM_ID = ?
            """;

    private static final String GET_POPULAR_FILM_LIKES_SQL = """
            SELECT
              f.FILM_ID,
              f.FILM_NAME,
              f.FILM_DESCRIPTION,
              f.FILM_RELEASEDATE,
              f.FILM_DURATION,
              f.RATING_ID,
              m.MPA_ID,
              m.MPA_NAME,
              COUNT(fl.*) AS likes
            FROM FILM f
            LEFT JOIN MPA m ON m.MPA_ID = f.RATING_ID
            LEFT JOIN FILM_LIKES fl ON fl.FILM_ID = f.FILM_ID
            GROUP BY f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION,
              f.FILM_RELEASEDATE, f.FILM_DURATION, f.RATING_ID,
              m.MPA_ID, m.MPA_NAME
            ORDER BY likes DESC
            """;

    private static final String GET_POPULAR_FILM_LIKES_LIMIT_SQL = """
            SELECT
              f.FILM_ID,
              f.FILM_NAME,
              f.FILM_DESCRIPTION,
              f.FILM_RELEASEDATE,
              f.FILM_DURATION,
              f.RATING_ID,
              m.MPA_ID,
              m.MPA_NAME,
              COUNT(fl.*) AS likes
            FROM FILM f
            LEFT JOIN MPA m ON m.MPA_ID = f.RATING_ID
            LEFT JOIN FILM_LIKES fl ON fl.FILM_ID = f.FILM_ID
            GROUP BY f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION,
              f.FILM_RELEASEDATE, f.FILM_DURATION, f.RATING_ID,
              m.MPA_ID, m.MPA_NAME
            ORDER BY likes DESC
            LIMIT ?
            """;

    private final FilmRowMapper filmRowMapper;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         RowMapper<Film> rowMapper, FilmRowMapper filmRowMapper) {
        super(jdbcTemplate, rowMapper, Film.class);
        this.filmRowMapper = filmRowMapper;
    }

    @Override
    public Film create(Film film) {
        Long id = insert(CREATE_FILM_SQL,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null);

        film.setId(id);

        if (!film.getGenres().isEmpty()) {
            film.getGenres()
                    .stream()
                    .sorted(Comparator.comparingLong(g -> g.getId()))
                    .forEach(genre -> {
                jdbcTemplate.update(ADD_GENRE_TO_FILM_SQL,
                        film.getId(), genre.getId());
            });
        }
        return getFilmById(id).orElse(null);
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_FILM_SQL,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId());

        if (!film.getGenres().isEmpty()) {
            jdbcTemplate.update(DELETE_FILM_GENRES_SQL,
                    film.getId());

            film.getGenres()
                    .stream()
                    .sorted(Comparator.comparingLong(g -> g.getId()))
                    .forEach(genre -> {
                jdbcTemplate.update(ADD_GENRE_TO_FILM_SQL,
                        film.getId(), genre.getId());
            });
        }

        return getFilmById(film.getId()).orElse(null);
    }

    @Override
    public void delete(Long id) {
        delete(DELETE_FILM_SQL, id);
    }

    @Override
    public List<Film> getAllFilms() {
        return findMany(FIND_ALL_FILMS_SQL);
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return findOne(FIND_FILM_BY_ID_SQL, id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.update(ADD_FILM_LIKE_SQL,
                filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        jdbcTemplate.update(DELETE_FILM_LIKE_SQL,
                filmId, userId);
    }

    @Override
    public Integer getLikesCount(Long filmId) {
        Integer count = jdbcTemplate.queryForObject(GET_FILM_LIKES_SQL, Integer.class, filmId);
        return count != null ? count : 0;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        if (count == null) {
            return findMany(GET_POPULAR_FILM_LIKES_SQL);
        } else {
            return jdbcTemplate.query(GET_POPULAR_FILM_LIKES_LIMIT_SQL, filmRowMapper, count);
        }
    }
}
