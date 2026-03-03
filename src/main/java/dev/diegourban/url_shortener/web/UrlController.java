package dev.diegourban.url_shortener.web;

import java.util.Optional;

import dev.diegourban.url_shortener.application.command.CreateShortUrlCommand;
import dev.diegourban.url_shortener.domain.service.UrlShorteningService;
import dev.diegourban.urlshortener.api.RedirectApi;
import dev.diegourban.urlshortener.api.UrlApi;
import dev.diegourban.urlshortener.model.CreateShortUrlRequest;
import dev.diegourban.urlshortener.model.CreateShortUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

@RestController
@RequiredArgsConstructor
public class UrlController implements UrlApi, RedirectApi {

    private final UrlShorteningService urlShorteningService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<CreateShortUrlResponse> createShortUrl(final CreateShortUrlRequest createShortUrlRequest) {
        final var shortUrl = urlShorteningService.createShortUrl(new CreateShortUrlCommand(createShortUrlRequest.getLongUrl()));
        final var createShortUrlResponse = new CreateShortUrlResponse().shortCode(shortUrl.code()).shortUrl(shortUrl.shortUrl());
        return ResponseEntity.created(createShortUrlResponse.getShortUrl()).body(createShortUrlResponse);
    }

    @Override
    public ResponseEntity<Void> redirectToLongUrl(final String shortCode) {
        return urlShorteningService.resolveShortUrl(shortCode)
                .map(originalUrl -> ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, originalUrl.toString())
                        .<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
