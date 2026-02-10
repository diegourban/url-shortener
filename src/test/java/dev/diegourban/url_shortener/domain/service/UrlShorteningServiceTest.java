package dev.diegourban.url_shortener.domain.service;

import java.net.URI;

import dev.diegourban.url_shortener.AbstractUnitTest;
import dev.diegourban.url_shortener.application.command.CreateShortUrlCommand;
import dev.diegourban.url_shortener.config.UrlShortenerProperties;
import dev.diegourban.url_shortener.infra.persistence.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UrlShorteningServiceTest extends AbstractUnitTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private UrlShortenerProperties urlShortenerProperties;

    @InjectMocks
    private UrlShorteningService urlShorteningService;

    @Test
    void test() {
        when(shortUrlRepository.save(any())).thenReturn(1L);
        when(urlShortenerProperties.getBaseUrl()).thenReturn("http://localhost");

        final var createShortUrlCommand = new CreateShortUrlCommand(URI.create("https://www.example.com"));
        final var shortUrl = urlShorteningService.createShortUrl(createShortUrlCommand);
        assertThat(shortUrl.code()).isEqualTo("AAAAAAAAAAE");
        assertThat(shortUrl.shortUrl()).isEqualTo(URI.create("http://localhost/AAAAAAAAAAE"));
    }
}
