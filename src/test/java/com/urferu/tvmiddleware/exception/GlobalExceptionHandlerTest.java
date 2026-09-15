package com.urferu.tvmiddleware.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MissingServletRequestParameterException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest();

    @BeforeEach
    void setUp() {
        request.setRequestURI("/api/shows/99");
    }

    @Test
    void showNotFoundReturns404() {
        ResponseEntity<ErrorResponse> response =
                handler.handleShowNotFound(new ShowNotFoundException(99L), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().message()).isEqualTo("No se encontró el show con id 99");
        assertThat(response.getBody().path()).isEqualTo("/api/shows/99");
    }

    @Test
    void tvMazeFailureReturns502() {
        ResponseEntity<ErrorResponse> response =
                handler.handleTvMazeClient(new TvMazeClientException("TV Maze no respondió"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(502);
    }

    @Test
    void missingQueryParamReturns400() {
        ResponseEntity<ErrorResponse> response = handler.handleMissingParam(
                new MissingServletRequestParameterException("search_query", "String"),
                request
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
    }
}
