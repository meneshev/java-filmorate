package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.BaseStorage;
import java.util.List;
import java.util.Optional;

@Repository
public class MPADbStorage extends BaseStorage<MPA> implements MPAStorage {
    private static final String getMPAByIdSql = """
            SELECT
            	MPA_ID,
            	MPA_NAME
            FROM MPA
            WHERE MPA_ID = ?
            """;

    private static final String getAllMPAsSql = """
            SELECT
            	MPA_ID,
            	MPA_NAME
            FROM MPA
            ORDER BY MPA_ID
            """;

    public MPADbStorage(JdbcTemplate jdbcTemplate, RowMapper<MPA> rowMapper) {
        super(jdbcTemplate, rowMapper, MPA.class);
    }

    @Override
    public Optional<MPA> getMPAById(Long id) {
        return findOne(getMPAByIdSql, id);
    }

    @Override
    public List<MPA> getAllMPAs() {
        return findMany(getAllMPAsSql);
    }
}
