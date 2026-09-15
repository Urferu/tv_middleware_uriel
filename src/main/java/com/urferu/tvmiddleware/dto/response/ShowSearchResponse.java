package com.urferu.tvmiddleware.dto.response;

import java.util.List;

public record ShowSearchResponse(
        Long id,
        String name,
        String channel,
        String summary,
        List<String> genres,
        List<CommentResponse> comments
) {
}
