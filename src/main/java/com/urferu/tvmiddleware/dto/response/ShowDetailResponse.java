package com.urferu.tvmiddleware.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeShowDto;

public record ShowDetailResponse(
        @JsonUnwrapped TvMazeShowDto show,
        List<CommentResponse> comments
) {
}
