package com.urferu.tvmiddleware.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.urferu.tvmiddleware.dto.response.ShowSearchResponse;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeShowDto;

@Component
public class ShowMapper {

    /**
     * Devuelve el canal visible del show: primero el network y, si no hay, el webChannel.
     */
    public String resolveChannel(TvMazeShowDto show) {
        if (show == null) {
            return null;
        }
        if (show.network() != null && show.network().name() != null && !show.network().name().isBlank()) {
            return show.network().name();
        }
        if (show.webChannel() != null && show.webChannel().name() != null && !show.webChannel().name().isBlank()) {
            return show.webChannel().name();
        }
        return null;
    }

    public ShowSearchResponse toSearchResponse(TvMazeShowDto show) {
        return new ShowSearchResponse(
                show.id(),
                show.name(),
                resolveChannel(show),
                show.summary(),
                show.genres() == null ? List.of() : show.genres()
        );
    }
}
