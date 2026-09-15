package com.urferu.tvmiddleware.dto.tvmaze;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvMazeShowDto(
        Long id,
        String url,
        String name,
        String type,
        String language,
        List<String> genres,
        String status,
        Integer runtime,
        Integer averageRuntime,
        String premiered,
        String ended,
        String officialSite,
        TvMazeScheduleDto schedule,
        TvMazeRatingDto rating,
        Integer weight,
        TvMazeNetworkDto network,
        TvMazeWebChannelDto webChannel,
        Object dvdCountry,
        TvMazeExternalsDto externals,
        TvMazeImageDto image,
        String summary,
        Long updated,
        @JsonProperty("_links") TvMazeLinksDto links
) {
}
