package com.urferu.tvmiddleware.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.urferu.tvmiddleware.dto.request.CommentRequest;
import com.urferu.tvmiddleware.dto.response.CommentResponse;
import com.urferu.tvmiddleware.dto.response.StatusResponse;
import com.urferu.tvmiddleware.model.Comment;
import com.urferu.tvmiddleware.repository.CommentRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void addCommentPersistsAndReturnsStatus() {
        StatusResponse response = commentService.addComment(1L, new CommentRequest("Muy buena", 5));

        assertThat(response.status()).isEqualTo("CREATED");
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void findByShowIdMapsStoredComments() {
        when(commentRepository.findByShowId(1L)).thenReturn(List.of(new Comment(1L, "Muy buena", 5)));

        List<CommentResponse> comments = commentService.findByShowId(1L);

        assertThat(comments).containsExactly(new CommentResponse("Muy buena", 5));
    }
}
