package com.urferu.tvmiddleware.controller;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urferu.tvmiddleware.dto.response.ShowDetailResponse;
import com.urferu.tvmiddleware.dto.response.ShowSearchResponse;
import com.urferu.tvmiddleware.service.ShowService;

@Validated
@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/search")
    public List<ShowSearchResponse> search(
            @RequestParam("search_query") @NotBlank String searchQuery) {
        return showService.search(searchQuery);
    }

    @GetMapping("/{showId}")
    public ShowDetailResponse getShow(@PathVariable Long showId) {
        return showService.getShow(showId);
    }
}
