package com.urferu.tvmiddleware.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.urferu.tvmiddleware.client.TvMazeClient;
import com.urferu.tvmiddleware.dto.response.ShowSearchResponse;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeNetworkDto;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeSearchItemDto;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeShowDto;
import com.urferu.tvmiddleware.mapper.ShowMapper;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

    @Mock
    private TvMazeClient tvMazeClient;

    @Mock
    private ShowMapper showMapper;

    @InjectMocks
    private ShowService showService;

    @Test
    void searchMapsEachShowFromTvMaze() {
        TvMazeShowDto show = new TvMazeShowDto(
                139L, null, "Girls", null, null, List.of("Drama"), null, null, null, null, null,
                new TvMazeNetworkDto(8, "HBO"), null, "Resumen"
        );
        ShowSearchResponse mapped = new ShowSearchResponse(139L, "Girls", "HBO", "Resumen", List.of("Drama"));

        when(tvMazeClient.searchShows("girls")).thenReturn(List.of(new TvMazeSearchItemDto(0.9, show)));
        when(showMapper.toSearchResponse(show)).thenReturn(mapped);

        List<ShowSearchResponse> result = showService.search("girls");

        assertThat(result).containsExactly(mapped);
    }

    @Test
    void searchIgnoresItemsWithoutShow() {
        when(tvMazeClient.searchShows("empty")).thenReturn(List.of(new TvMazeSearchItemDto(0.1, null)));

        assertThat(showService.search("empty")).isEmpty();
    }
}
