package com.urferu.tvmiddleware.dto.tvmaze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvMazeLinksDto(TvMazeLinkDto self, TvMazeLinkDto previousepisode, TvMazeLinkDto nextepisode) {
}
