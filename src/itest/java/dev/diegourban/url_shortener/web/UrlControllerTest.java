package dev.diegourban.url_shortener.web;

import java.net.URI;
import java.util.Objects;

import dev.diegourban.url_shortener.UrlShortenerApplicationTests;
import dev.diegourban.urlshortener.model.CreateShortUrlRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UrlControllerTest extends UrlShortenerApplicationTests {

    @Test
    void createShortUrl() throws Exception {
        final var createShortUrlRequest = new CreateShortUrlRequest().longUrl(URI.create("http://example.com/some/very/long/url"));

        mockMvc.perform(post(BASE_URL + "/urls")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createShortUrlRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode", matchesPattern("[A-Za-z0-9_-]+")))
                // check Location header starts with base URL
                .andExpect(header().string("Location", startsWith("http://localhost/")))
                // optional: check that code part is not empty
                .andExpect(result -> {
                    final var location = result.getResponse().getHeader("Location");
                    final var code = Objects.requireNonNull(location).substring("http://localhost/".length());
                    assertThat(code, matchesPattern("[A-Za-z0-9_-]+")); // matches Base64 URL-safe
                });
    }

    @Test
    void redirectToLongUrl() throws Exception {
        final var originalUrl = "http://example.com/some/very/long/url";
        final var createShortUrlRequest = new CreateShortUrlRequest().longUrl(URI.create(originalUrl));

        // Create a short URL first
        final var createResult = mockMvc.perform(post(BASE_URL + "/urls")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createShortUrlRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        final var location = createResult.getResponse().getHeader("Location");
        final var shortCode = Objects.requireNonNull(location).substring("http://localhost/".length());

        // Resolve the short URL
        mockMvc.perform(get("/" + shortCode))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));
    }

    @Test
    void redirectToLongUrl_notFound() throws Exception {
        mockMvc.perform(get("/f_____8"))
                .andExpect(status().isNotFound());
    }
}