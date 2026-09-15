package com.urferu.tvmiddleware.dto.tvmaze;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
        String premiered,
        String ended,
        String officialSite,
        TvMazeNetworkDto network,
        TvMazeWebChannelDto webChannel,
        String summary
) {
}
