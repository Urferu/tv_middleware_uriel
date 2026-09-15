package com.urferu.tvmiddleware.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.urferu.tvmiddleware.dto.response.ShowSearchResponse;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeShowDto;
import com.urferu.tvmiddleware.support.ShowFixtures;

class ShowMapperTest {

    private final ShowMapper mapper = new ShowMapper();

    @Test
    void resolveChannelPrefersNetwork() {
        TvMazeShowDto show = ShowFixtures.show(1L, "Demo", "HBO", "Apple TV", null, List.of());

        assertThat(mapper.resolveChannel(show)).isEqualTo("HBO");
    }

    @Test
    void resolveChannelFallsBackToWebChannel() {
        TvMazeShowDto show = ShowFixtures.show(1L, "Demo", null, "Apple TV", null, List.of());

        assertThat(mapper.resolveChannel(show)).isEqualTo("Apple TV");
    }

    @Test
    void toSearchResponseMapsExpectedFields() {
        TvMazeShowDto show = ShowFixtures.show(
                139L, "Girls", "HBO", null, "<p>Una serie de HBO</p>", List.of("Drama", "Romance")
        );

        ShowSearchResponse response = mapper.toSearchResponse(show);

        assertThat(response.id()).isEqualTo(139L);
        assertThat(response.name()).isEqualTo("Girls");
        assertThat(response.channel()).isEqualTo("HBO");
        assertThat(response.summary()).isEqualTo("<p>Una serie de HBO</p>");
        assertThat(response.genres()).containsExactly("Drama", "Romance");
        assertThat(response.comments()).isEmpty();
    }
}
