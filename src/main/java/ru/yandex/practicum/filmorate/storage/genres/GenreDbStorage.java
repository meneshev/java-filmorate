package ru.yandex.practicum.filmorate.storage.genres;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseStorage;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseStorage<Genre> implements GenresStorage {
    private static final String GET_GENRE_BY_ID_SQL = """
            SELECT
            	GENRE_ID,
            	GENRE_NAME
            FROM GENRE
            WHERE GENRE_ID = ?
            """;

    private static final String GET_ALL_GENRES_SQL = """
            SELECT
            	GENRE_ID,
            	GENRE_NAME
            FROM GENRE
            ORDER BY GENRE_ID
            """;

    public GenreDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Genre> rowMapper) {
        super(jdbcTemplate, rowMapper, Genre.class);
    }

    @Override
    public Optional<Genre> getGenreById(Long genreId) {
        return findOne(GET_GENRE_BY_ID_SQL, genreId);
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(GET_ALL_GENRES_SQL);
    }
}
