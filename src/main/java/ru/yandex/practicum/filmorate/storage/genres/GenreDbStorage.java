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
    private static final String getGenreByIdSql = """
            SELECT
            	GENRE_ID,
            	GENRE_NAME
            FROM GENRE
            WHERE GENRE_ID = ?
            """;

    private static final String getAllGenresSql = """
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
        return findOne(getGenreByIdSql, genreId);
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(getAllGenresSql);
    }
}
