package com.urferu.tvmiddleware.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.urferu.tvmiddleware.client.TvMazeClient;
import com.urferu.tvmiddleware.dto.response.ShowSearchResponse;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeSearchItemDto;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeShowDto;
import com.urferu.tvmiddleware.exception.ShowNotFoundException;
import com.urferu.tvmiddleware.mapper.ShowMapper;

@Service
public class ShowService {

    private final TvMazeClient tvMazeClient;
    private final ShowMapper showMapper;

    public ShowService(TvMazeClient tvMazeClient, ShowMapper showMapper) {
        this.tvMazeClient = tvMazeClient;
        this.showMapper = showMapper;
    }

    public List<ShowSearchResponse> search(String searchQuery) {
        return tvMazeClient.searchShows(searchQuery).stream()
                .map(TvMazeSearchItemDto::show)
                .filter(Objects::nonNull)
                .map(showMapper::toSearchResponse)
                .toList();
    }

    public TvMazeShowDto getShow(Long showId) {
        TvMazeShowDto show = tvMazeClient.getShow(showId);
        if (show == null) {
            throw new ShowNotFoundException(showId);
        }
        return show;
    }
}
