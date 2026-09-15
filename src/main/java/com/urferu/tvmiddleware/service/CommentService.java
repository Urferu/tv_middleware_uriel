package com.urferu.tvmiddleware.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.urferu.tvmiddleware.dto.request.CommentRequest;
import com.urferu.tvmiddleware.dto.response.CommentResponse;
import com.urferu.tvmiddleware.dto.response.StatusResponse;
import com.urferu.tvmiddleware.model.Comment;
import com.urferu.tvmiddleware.repository.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public StatusResponse addComment(Long showId, CommentRequest request) {
        commentRepository.save(new Comment(showId, request.comment(), request.rating()));
        return new StatusResponse("CREATED", "Comentario guardado");
    }

    /**
     * Lista los comentarios persistidos de un show, listos para anexarlos a search o detail.
     */
    public List<CommentResponse> findByShowId(Long showId) {
        return commentRepository.findByShowId(showId).stream()
                .map(comment -> new CommentResponse(comment.getComment(), comment.getRating()))
                .toList();
    }
}
