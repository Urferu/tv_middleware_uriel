package com.urferu.tvmiddleware.support;

import java.util.List;

import com.urferu.tvmiddleware.dto.tvmaze.TvMazeNetworkDto;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeShowDto;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeWebChannelDto;

public final class ShowFixtures {

    private ShowFixtures() {
    }

    public static TvMazeShowDto show(
            Long id,
            String name,
            String networkName,
            String webChannelName,
            String summary,
            List<String> genres
    ) {
        return new TvMazeShowDto(
                id,
                null,
                name,
                null,
                null,
                genres,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                networkName == null ? null : new TvMazeNetworkDto(1, networkName),
                webChannelName == null ? null : new TvMazeWebChannelDto(1, webChannelName),
                null,
                null,
                null,
                summary,
                null,
                null
        );
    }
}
