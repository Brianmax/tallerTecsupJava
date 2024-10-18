package org.ravn.moviescatalogchallenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ravn.moviescatalogchallenge.aggregate.request.RateDto;
import org.ravn.moviescatalogchallenge.aggregate.response.ResponseBase;
import org.ravn.moviescatalogchallenge.service.RateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rate")
@Tag(name = "Rate", description = "Rate a movie, get movie rates and delete a movie rate")
public class RateController {
    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }
    @Operation(summary = "Rate a movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie successfully rated, returns the movie with its rate",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseBase.class),
                    examples = @ExampleObject(value ="{\n    \"message\": \"Rate saved\",\n    \"status\": 200,\n    \"errors\": [],\n    \"data\": {\n        \"rate\": 5.0,\n        \"movieName\": \"The Dark Knight\"\n    }\n}" ))),
            @ApiResponse(responseCode = "400", description = "Invalid request, missing required fields. Check the response message",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseBase.class),
                    examples = @ExampleObject(value = "{\n    \"message\": \"Error saving rate\",\n    \"status\": 400,\n    \"errors\": [\n        \"Rate must be between 0 and 5\"\n    ],\n    \"data\": null\n}"))),
    })
    @PostMapping("/save")
    ResponseBase<RateDto> rateMovie(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Movie name and rate. Rate must be between 0 and 5", required = true)
            @RequestBody RateDto rateDto) {
        return rateService.rateMovie(rateDto);
    }

    @Operation(summary = "Get movie rates for authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie rates successfully retrieved, returns the list of movies with their rates",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = RateDto.class),
                    examples = @ExampleObject(value = "[\n    {\n        \"rate\": 5.0,\n        \"movieName\": \"The Dark Knight\"\n    },\n    {\n        \"rate\": 4.0,\n        \"movieName\": \"Inception\"\n    }\n]"))),
            @ApiResponse(responseCode = "400", description = "Invalid request, missing required fields. Check the response message",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseBase.class),
                    examples = @ExampleObject(value = "{\n    \"message\": \"Error getting rates\",\n    \"status\": 400,\n    \"errors\": [\n        \"No rates found\"\n    ],\n    \"data\": null\n}"))),
    })
    @GetMapping("/get")
    List<RateDto> getMovieRates() {
        return rateService.getMovieRates();
    }
    @Operation(summary = "Delete a movie rate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie rate successfully deleted, returns true",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseBase.class),
                    examples = @ExampleObject(value = "{\n    \"message\": \"Rate deleted\",\n    \"status\": 200,\n    \"errors\": [],\n    \"data\": true\n}"))),
            @ApiResponse(responseCode = "400", description = "Invalid request, missing required fields. Check the response message",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseBase.class),
                    examples = @ExampleObject(value = "{\n    \"message\": \"Error deleting rate\",\n    \"status\": 400,\n    \"errors\": [\n        \"Rate not found\"\n    ],\n    \"data\": null\n}"))),
    })
    @DeleteMapping("/delete")
    ResponseBase<Boolean> deleteRate(
            @Parameter(description = "Movie name", required = true)
            @RequestParam String movieName) {
        return rateService.deleteRate(movieName);
    }
}
