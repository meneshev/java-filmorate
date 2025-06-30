package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    private static final String getGenresByFilmId = """
            SELECT
                g.GENRE_ID,
                g.GENRE_NAME
            FROM FILM_GENRES fg
            LEFT JOIN GENRE g ON fg.GENRE_ID = g.GENRE_ID
            WHERE fg.FILM_ID = ?
            """;

    private static final String getLikesByFilmId = """
            SELECT
                fl.USER_ID
            FROM FILM_LIKES fl
            WHERE fl.FILM_ID = ?
            """;

    private static final String createUserSql = """
            INSERT INTO PUBLIC.`USER`
            (USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY)
            VALUES(?, ?, ?, ?);
            """;

    private final JdbcTemplate jdbcTemplate;

    public FilmRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("FILM_NAME"));
        film.setDescription(rs.getString("FILM_DESCRIPTION"));
        film.setReleaseDate(rs.getDate("FILM_RELEASEDATE").toLocalDate());
        film.setDuration(Duration.ofMinutes(rs.getInt("FILM_DURATION")));

        if (rs.getObject("RATING_ID") != null) {
            MPA mpa = new MPA();
            mpa.setId(rs.getLong("MPA_ID"));
            mpa.setName(rs.getString("MPA_NAME"));
            film.setMpa(mpa);
        }

        Set<Genre> genres = new LinkedHashSet<>(
                jdbcTemplate.query(getGenresByFilmId, new GenreRowMapper(), film.getId())
        );
        film.setGenres(genres);

        Set<Long> likes = new HashSet<>(
                jdbcTemplate.queryForList(getLikesByFilmId, Long.class, film.getId())
        );
        film.setLikes(likes);

        return film;
    }
}
