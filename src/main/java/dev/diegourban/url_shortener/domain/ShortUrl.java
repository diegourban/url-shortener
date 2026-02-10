package dev.diegourban.url_shortener.domain;

import java.net.URI;

public record ShortUrl(String code, URI shortUrl) {

}
