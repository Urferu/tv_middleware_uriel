package com.urferu.tvmiddleware.dto.tvmaze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvMazeSearchItemDto(Double score, TvMazeShowDto show) {
}
