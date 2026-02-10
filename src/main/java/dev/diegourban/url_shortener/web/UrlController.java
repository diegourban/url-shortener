package dev.diegourban.url_shortener.web;

import dev.diegourban.url_shortener.application.command.CreateShortUrlCommand;
import dev.diegourban.url_shortener.domain.service.UrlShorteningService;
import dev.diegourban.urlshortener.api.UrlApi;
import dev.diegourban.urlshortener.model.CreateShortUrlRequest;
import dev.diegourban.urlshortener.model.CreateShortUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UrlController implements UrlApi {

    private final UrlShorteningService urlShorteningService;

    @Override
    public ResponseEntity<CreateShortUrlResponse> createShortUrl(final CreateShortUrlRequest createShortUrlRequest) {
        final var shortUrl = urlShorteningService.createShortUrl(new CreateShortUrlCommand(createShortUrlRequest.getLongUrl()));
        final var createShortUrlResponse = new CreateShortUrlResponse().shortCode(shortUrl.code()).shortUrl(shortUrl.shortUrl());
        return ResponseEntity.created(createShortUrlResponse.getShortUrl()).body(createShortUrlResponse);
    }

}
