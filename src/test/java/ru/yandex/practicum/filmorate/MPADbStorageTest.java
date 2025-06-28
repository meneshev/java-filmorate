package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mappers.MPARowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MPADbStorage;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({MPADbStorage.class, MPARowMapper.class})
public class MPADbStorageTest {
    @Autowired
    private MPADbStorage mpaDbStorage;

    @Test
    void testGetMPAById() {
        Optional<MPA> foundMPA = mpaDbStorage.getMPAById(1L);

        assertThat(foundMPA)
                .isPresent()
                .hasValueSatisfying(genre -> {
                    assertThat(genre.getId()).isEqualTo(1L);
                    assertThat(genre.getName()).isEqualTo("G");
                });
    }

    @Test
    void shouldGetAllMPAs() {
        List<MPA> MPAs = mpaDbStorage.getAllMPAs();

        assertThat(MPAs)
                .extracting(MPA::getName)
                .contains("G", "PG");
    }
}
