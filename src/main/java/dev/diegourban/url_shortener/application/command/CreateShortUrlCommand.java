package dev.diegourban.url_shortener.application.command;

import java.net.URI;

public record CreateShortUrlCommand(URI longUrl) {
}
