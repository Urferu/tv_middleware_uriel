package com.urferu.tvmiddleware.dto.tvmaze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvMazeExternalsDto(Integer tvrage, Integer thetvdb, String imdb) {
}
