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
import com.urferu.tvmiddleware.dto.response.CommentResponse;
import com.urferu.tvmiddleware.dto.response.ShowDetailResponse;
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

    @Mock
    private CommentService commentService;

    @InjectMocks
    private ShowService showService;

    @Test
    void searchMapsEachShowFromTvMaze() {
        TvMazeShowDto show = ShowFixtures.show(139L, "Girls", "HBO", null, "Resumen", List.of("Drama"));
        List<CommentResponse> comments = List.of(new CommentResponse("Muy buena", 5));
        ShowSearchResponse mapped = new ShowSearchResponse(139L, "Girls", "HBO", "Resumen", List.of("Drama"), comments);

        when(tvMazeClient.searchShows("girls")).thenReturn(List.of(new TvMazeSearchItemDto(0.9, show)));
        when(commentService.findByShowId(139L)).thenReturn(comments);
        when(showMapper.toSearchResponse(show, comments)).thenReturn(mapped);

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
        when(commentService.findByShowId(1L)).thenReturn(List.of(new CommentResponse("Top", 5)));

        ShowDetailResponse result = showService.getShow(1L);

        assertThat(result.show()).isEqualTo(cached);
        assertThat(result.comments()).containsExactly(new CommentResponse("Top", 5));
        verify(tvMazeClient, never()).getShow(1L);
        verify(showCacheRepository, never()).save(any());
    }

    @Test
    void getShowFetchesAndCachesWhenMissing() {
        TvMazeShowDto show = ShowFixtures.show(1L, "Under the Dome", "CBS", null, "Resumen", List.of("Drama"));
        when(showCacheRepository.findById(1L)).thenReturn(Optional.empty());
        when(tvMazeClient.getShow(1L)).thenReturn(show);
        when(commentService.findByShowId(1L)).thenReturn(List.of());

        ShowDetailResponse result = showService.getShow(1L);

        assertThat(result.show()).isEqualTo(show);
        assertThat(result.comments()).isEmpty();
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
