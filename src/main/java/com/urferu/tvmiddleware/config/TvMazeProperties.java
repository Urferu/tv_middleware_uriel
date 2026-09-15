package com.urferu.tvmiddleware.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tvmaze")
public record TvMazeProperties(String baseUrl) {
}
