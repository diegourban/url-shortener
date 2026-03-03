package dev.diegourban.url_shortener.domain.service;

import java.net.URI;
import java.util.Optional;

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
        assertThat(shortUrl.code()).isEqualTo("AQ");
        assertThat(shortUrl.shortUrl()).isEqualTo(URI.create("http://localhost/AQ"));
    }

    @Test
    void resolveShortUrl_existingCode_returnsOriginalUrl() {
        when(shortUrlRepository.findOriginalUrlById(1L)).thenReturn(Optional.of("https://www.example.com"));

        final var result = urlShorteningService.resolveShortUrl("AQ");

        assertThat(result).contains(URI.create("https://www.example.com"));
    }

    @Test
    void resolveShortUrl_unknownCode_returnsEmpty() {
        when(shortUrlRepository.findOriginalUrlById(1L)).thenReturn(Optional.empty());

        final var result = urlShorteningService.resolveShortUrl("AQ");

        assertThat(result).isEmpty();
    }
}
