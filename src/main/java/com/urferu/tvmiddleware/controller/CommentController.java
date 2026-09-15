package com.urferu.tvmiddleware.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.urferu.tvmiddleware.dto.request.CommentRequest;
import com.urferu.tvmiddleware.dto.response.StatusResponse;
import com.urferu.tvmiddleware.service.CommentService;

@RestController
@RequestMapping("/api/shows")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{showId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public StatusResponse addComment(
            @PathVariable Long showId,
            @Valid @RequestBody CommentRequest request
    ) {
        return commentService.addComment(showId, request);
    }
}
