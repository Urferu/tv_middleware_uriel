package com.urferu.tvmiddleware.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.urferu.tvmiddleware.dto.request.CommentRequest;
import com.urferu.tvmiddleware.dto.response.StatusResponse;
import com.urferu.tvmiddleware.exception.GlobalExceptionHandler;
import com.urferu.tvmiddleware.service.CommentService;

@WebMvcTest(CommentController.class)
@Import(GlobalExceptionHandler.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Test
    void addCommentReturnsCreatedStatus() throws Exception {
        when(commentService.addComment(eq(1L), any(CommentRequest.class)))
                .thenReturn(new StatusResponse("CREATED", "Comentario guardado"));

        mockMvc.perform(post("/api/shows/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Muy buena\",\"rating\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void addCommentRejectsRatingAboveFive() throws Exception {
        mockMvc.perform(post("/api/shows/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Regular\",\"rating\":8}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void addCommentRejectsBlankComment() throws Exception {
        mockMvc.perform(post("/api/shows/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"\",\"rating\":3}"))
                .andExpect(status().isBadRequest());
    }
}
