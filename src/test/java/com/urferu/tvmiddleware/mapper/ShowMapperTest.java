package com.urferu.tvmiddleware.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.urferu.tvmiddleware.dto.response.ShowSearchResponse;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeNetworkDto;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeShowDto;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeWebChannelDto;

class ShowMapperTest {

    private final ShowMapper mapper = new ShowMapper();

    @Test
    void resolveChannelPrefersNetwork() {
        TvMazeShowDto show = show("HBO", "Apple TV");

        assertThat(mapper.resolveChannel(show)).isEqualTo("HBO");
    }

    @Test
    void resolveChannelFallsBackToWebChannel() {
        TvMazeShowDto show = show(null, "Apple TV");

        assertThat(mapper.resolveChannel(show)).isEqualTo("Apple TV");
    }

    @Test
    void toSearchResponseMapsExpectedFields() {
        TvMazeShowDto show = new TvMazeShowDto(
                139L,
                "https://www.tvmaze.com/shows/139/girls",
                "Girls",
                "Scripted",
                "English",
                List.of("Drama", "Romance"),
                "Ended",
                30,
                "2012-04-15",
                "2017-04-16",
                "http://www.hbo.com/girls",
                new TvMazeNetworkDto(8, "HBO"),
                null,
                "<p>Una serie de HBO</p>"
        );

        ShowSearchResponse response = mapper.toSearchResponse(show);

        assertThat(response.id()).isEqualTo(139L);
        assertThat(response.name()).isEqualTo("Girls");
        assertThat(response.channel()).isEqualTo("HBO");
        assertThat(response.summary()).isEqualTo("<p>Una serie de HBO</p>");
        assertThat(response.genres()).containsExactly("Drama", "Romance");
    }

    private TvMazeShowDto show(String networkName, String webChannelName) {
        return new TvMazeShowDto(
                1L, null, "Demo", null, null, List.of(), null, null, null, null, null,
                networkName == null ? null : new TvMazeNetworkDto(1, networkName),
                webChannelName == null ? null : new TvMazeWebChannelDto(1, webChannelName),
                null
        );
    }
}
