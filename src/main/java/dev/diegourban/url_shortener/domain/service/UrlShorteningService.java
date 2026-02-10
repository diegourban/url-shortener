package dev.diegourban.url_shortener.domain.service;

import java.net.URI;
import java.nio.ByteBuffer;

import dev.diegourban.url_shortener.application.command.CreateShortUrlCommand;
import dev.diegourban.url_shortener.config.UrlShortenerProperties;
import dev.diegourban.url_shortener.domain.ShortUrl;
import dev.diegourban.url_shortener.infra.persistence.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShorteningService {

    private final ShortUrlRepository shortUrlRepository;
    private final UrlShortenerProperties urlShortenerProperties;

    private final Base64 base64 = Base64.builder().setUrlSafe(true).get();

    public ShortUrl createShortUrl(final CreateShortUrlCommand createShortUrlCommand) {
        final long id = shortUrlRepository.save(createShortUrlCommand.longUrl().toString());

        final byte[] array = ByteBuffer.allocate(Long.BYTES).putLong(id).array();
        final String code = base64.encodeToString(array);
        return new ShortUrl(code, URI.create(urlShortenerProperties.getBaseUrl() + "/" + code));
    }

}
