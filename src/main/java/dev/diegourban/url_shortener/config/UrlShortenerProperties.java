package dev.diegourban.url_shortener.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "url-shortener")
@Component
@Getter
@Setter
public class UrlShortenerProperties {

    private String baseUrl;
}
