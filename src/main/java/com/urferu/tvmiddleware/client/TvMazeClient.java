package com.urferu.tvmiddleware.client;

import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.urferu.tvmiddleware.dto.tvmaze.TvMazeSearchItemDto;
import com.urferu.tvmiddleware.dto.tvmaze.TvMazeShowDto;
import com.urferu.tvmiddleware.exception.ShowNotFoundException;
import com.urferu.tvmiddleware.exception.TvMazeClientException;

@Component
public class TvMazeClient {

    private final RestClient restClient;

    public TvMazeClient(RestClient tvMazeRestClient) {
        this.restClient = tvMazeRestClient;
    }

    public List<TvMazeSearchItemDto> searchShows(String query) {
        try {
            TvMazeSearchItemDto[] results = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/search/shows").queryParam("q", query).build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        throw new TvMazeClientException(
                                "TV Maze respondió " + response.getStatusCode().value() + " al buscar shows");
                    })
                    .body(TvMazeSearchItemDto[].class);
            return results == null ? List.of() : List.of(results);
        } catch (TvMazeClientException ex) {
            throw ex;
        } catch (RestClientException ex) {
            throw new TvMazeClientException("No se pudo consultar TV Maze", ex);
        }
    }

    public TvMazeShowDto getShow(Long showId) {
        try {
            return restClient.get()
                    .uri("/shows/{id}", showId)
                    .retrieve()
                    .onStatus(status -> status.value() == 404, (request, response) -> {
                        throw new ShowNotFoundException(showId);
                    })
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        throw new TvMazeClientException(
                                "TV Maze respondió " + response.getStatusCode().value() + " al consultar el show");
                    })
                    .body(TvMazeShowDto.class);
        } catch (ShowNotFoundException | TvMazeClientException ex) {
            throw ex;
        } catch (RestClientException ex) {
            throw new TvMazeClientException("No se pudo consultar TV Maze", ex);
        }
    }
}
