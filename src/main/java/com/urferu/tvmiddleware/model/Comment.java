package com.urferu.tvmiddleware.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    @Indexed
    private Long showId;
    private String comment;
    private Integer rating;
    private Instant createdAt;

    public Comment() {
    }

    public Comment(Long showId, String comment, Integer rating) {
        this.showId = showId;
        this.comment = comment;
        this.rating = rating;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public Long getShowId() {
        return showId;
    }

    public String getComment() {
        return comment;
    }

    public Integer getRating() {
        return rating;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
