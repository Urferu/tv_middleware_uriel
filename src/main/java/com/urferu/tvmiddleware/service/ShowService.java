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
import com.urferu.tvmiddleware.model.ShowCache;
import com.urferu.tvmiddleware.repository.ShowCacheRepository;

@Service
public class ShowService {

    private final TvMazeClient tvMazeClient;
    private final ShowMapper showMapper;
    private final ShowCacheRepository showCacheRepository;

    public ShowService(
            TvMazeClient tvMazeClient,
            ShowMapper showMapper,
            ShowCacheRepository showCacheRepository
    ) {
        this.tvMazeClient = tvMazeClient;
        this.showMapper = showMapper;
        this.showCacheRepository = showCacheRepository;
    }

    public List<ShowSearchResponse> search(String searchQuery) {
        return tvMazeClient.searchShows(searchQuery).stream()
                .map(TvMazeSearchItemDto::show)
                .filter(Objects::nonNull)
                .map(showMapper::toSearchResponse)
                .toList();
    }

    public TvMazeShowDto getShow(Long showId) {
        return showCacheRepository.findById(showId)
                .map(ShowCache::getPayload)
                .orElseGet(() -> fetchAndCache(showId));
    }

    /**
     * Si el show no está en Mongo, lo pide a TV Maze y lo deja cacheado para la próxima.
     */
    private TvMazeShowDto fetchAndCache(Long showId) {
        TvMazeShowDto show = tvMazeClient.getShow(showId);
        if (show == null) {
            throw new ShowNotFoundException(showId);
        }
        showCacheRepository.save(new ShowCache(show));
        return show;
    }
}
