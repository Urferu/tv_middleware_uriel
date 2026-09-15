package com.urferu.tvmiddleware.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.urferu.tvmiddleware.dto.response.CommentResponse;
import com.urferu.tvmiddleware.dto.response.ShowDetailResponse;
import com.urferu.tvmiddleware.dto.response.ShowSearchResponse;
import com.urferu.tvmiddleware.exception.GlobalExceptionHandler;
import com.urferu.tvmiddleware.exception.ShowNotFoundException;
import com.urferu.tvmiddleware.service.ShowService;
import com.urferu.tvmiddleware.support.ShowFixtures;

@WebMvcTest(ShowController.class)
@Import(GlobalExceptionHandler.class)
class ShowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShowService showService;

    @Test
    void searchReturnsShowArray() throws Exception {
        when(showService.search("girls")).thenReturn(List.of(
                new ShowSearchResponse(139L, "Girls", "HBO", "Resumen", List.of("Drama"), List.of())
        ));

        mockMvc.perform(get("/api/shows/search").param("search_query", "girls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(139))
                .andExpect(jsonPath("$[0].name").value("Girls"))
                .andExpect(jsonPath("$[0].channel").value("HBO"))
                .andExpect(jsonPath("$[0].genres[0]").value("Drama"))
                .andExpect(jsonPath("$[0].comments").isArray());
    }

    @Test
    void searchWithoutQueryReturns400() throws Exception {
        mockMvc.perform(get("/api/shows/search"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getShowReturnsCompleteShow() throws Exception {
        when(showService.getShow(1L)).thenReturn(new ShowDetailResponse(
                ShowFixtures.show(1L, "Under the Dome", "CBS", null, "Resumen", List.of("Drama")),
                List.of(new CommentResponse("Buen arranque", 4))
        ));

        mockMvc.perform(get("/api/shows/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Under the Dome"))
                .andExpect(jsonPath("$.network.name").value("CBS"))
                .andExpect(jsonPath("$.comments[0].comment").value("Buen arranque"))
                .andExpect(jsonPath("$.comments[0].rating").value(4));
    }

    @Test
    void getShowWhenMissingReturns404() throws Exception {
        when(showService.getShow(99L)).thenThrow(new ShowNotFoundException(99L));

        mockMvc.perform(get("/api/shows/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
