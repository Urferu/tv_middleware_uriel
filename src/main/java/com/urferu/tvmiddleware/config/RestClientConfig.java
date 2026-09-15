package com.urferu.tvmiddleware.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(TvMazeProperties.class)
public class RestClientConfig {

    @Bean
    RestClient tvMazeRestClient(TvMazeProperties properties, RestClient.Builder builder) {
        return builder
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.USER_AGENT, "tv-middleware/0.0.1")
                .build();
    }
}
