package com.urferu.tvmiddleware.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.urferu.tvmiddleware.dto.tvmaze.TvMazeShowDto;

@Document(collection = "shows")
public class ShowCache {

    @Id
    private Long id;
    private TvMazeShowDto payload;
    private Instant cachedAt;

    public ShowCache() {
    }

    public ShowCache(TvMazeShowDto show) {
        this.id = show.id();
        this.payload = show;
        this.cachedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public TvMazeShowDto getPayload() {
        return payload;
    }

    public Instant getCachedAt() {
        return cachedAt;
    }
}
