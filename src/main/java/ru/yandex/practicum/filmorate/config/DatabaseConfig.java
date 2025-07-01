package ru.yandex.practicum.filmorate.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Bean
    @Qualifier("filmJdbcTemplate")
    public JdbcTemplate filmJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
