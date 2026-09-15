package com.urferu.tvmiddleware.dto.tvmaze;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvMazeScheduleDto(String time, List<String> days) {
}
