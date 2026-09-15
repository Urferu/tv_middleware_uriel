package com.urferu.tvmiddleware.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.urferu.tvmiddleware.client.TvMazeClient;
import com.urferu.tvmiddleware.dto.response.ShowSearchResponse;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeSearchItemDto;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeShowDto;
import com.urferu.tvmiddleware.exception.ShowNotFoundException;
import com.urferu.tvmiddleware.mapper.ShowMapper;
import com.urferu.tvmiddleware.model.ShowCache;
import com.urferu.tvmiddleware.repository.ShowCacheRepository;
import com.urferu.tvmiddleware.support.ShowFixtures;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

    @Mock
    private TvMazeClient tvMazeClient;

    @Mock
    private ShowMapper showMapper;

    @Mock
    private ShowCacheRepository showCacheRepository;

    @InjectMocks
    private ShowService showService;

    @Test
    void searchMapsEachShowFromTvMaze() {
        TvMazeShowDto show = ShowFixtures.show(139L, "Girls", "HBO", null, "Resumen", List.of("Drama"));
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

    @Test
    void getShowReturnsCachedShowWithoutCallingTvMaze() {
        TvMazeShowDto cached = ShowFixtures.show(1L, "Under the Dome", "CBS", null, "Resumen", List.of("Drama"));
        when(showCacheRepository.findById(1L)).thenReturn(Optional.of(new ShowCache(cached)));

        assertThat(showService.getShow(1L)).isEqualTo(cached);
        verify(tvMazeClient, never()).getShow(1L);
        verify(showCacheRepository, never()).save(any());
    }

    @Test
    void getShowFetchesAndCachesWhenMissing() {
        TvMazeShowDto show = ShowFixtures.show(1L, "Under the Dome", "CBS", null, "Resumen", List.of("Drama"));
        when(showCacheRepository.findById(1L)).thenReturn(Optional.empty());
        when(tvMazeClient.getShow(1L)).thenReturn(show);

        assertThat(showService.getShow(1L)).isEqualTo(show);
        verify(showCacheRepository).save(any(ShowCache.class));
    }

    @Test
    void getShowThrowsWhenTvMazeReturnsNothing() {
        when(showCacheRepository.findById(99L)).thenReturn(Optional.empty());
        when(tvMazeClient.getShow(99L)).thenReturn(null);

        assertThatThrownBy(() -> showService.getShow(99L))
                .isInstanceOf(ShowNotFoundException.class)
                .hasMessageContaining("99");
        verify(showCacheRepository, never()).save(any());
    }
}
