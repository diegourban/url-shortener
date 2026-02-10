package dev.diegourban.url_shortener.infra.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShortUrlRepository {

    private final JdbcTemplate jdbcTemplate;

    public long save(final String originalUrl) {
        final var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final var ps = connection.prepareStatement("INSERT INTO short_url (original_url) VALUES (?)", new String[] {"id"});
            ps.setString(1, originalUrl);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
