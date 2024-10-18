package org.ravn.moviescatalogchallenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ravn.moviescatalogchallenge.aggregate.request.BaseMovieRequest;
import org.ravn.moviescatalogchallenge.aggregate.response.MovieResponse;
import org.ravn.moviescatalogchallenge.aggregate.response.ResponseBase;
import org.ravn.moviescatalogchallenge.service.MovieService;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movie")
@Tag(name = "Movie", description = "Movie management, create, update, delete and search movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(summary = "Create a new movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie successfully created, returns the movie",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovieResponse.class),
                    examples = @ExampleObject(value = "{\n    \"message\": \"Movie created successfully\",\n    \"status\": 200,\n    \"errors\": [],\n    \"data\": {\n        \"name\": \"It\",\n        \"releaseYear\": 2001,\n        \"synopsis\": \"Some synopsis\",\n        \"categories\": [\n            \"Horror\"\n        ],\n        \"createdBy\": \"admin@example.com\",\n        \"createdAt\": \"2024-09-04\",\n        \"updatedAt\": null,\n        \"updatedBy\": null,\n        \"deleted\": false,\n        \"deletedAt\": null,\n        \"deletedBy\": null,\n        \"presignedUrl\": \"The movie does not have an image\"\n    }\n}"))),
            @ApiResponse(responseCode = "400", description = "Invalid request, missing required fields. Check the response message",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBase.class),
                    examples = @ExampleObject(value = "{\n    \"message\": \"Error creating movie\",\n    \"status\": 400,\n    \"errors\": [\n        \"Name is required\"\n    ],\n    \"data\": null\n}"))),
    })
    @PostMapping("/admin/save")
    public ResponseBase<MovieResponse> saveMovie(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Movie name, release year, synopsis, categories and createdBy. The movie name must be unique. The token must be provided in the header", required = true)
            @RequestBody BaseMovieRequest movieCreateRequest) {
        return movieService.createMovie(movieCreateRequest);
    }

    @Operation(summary = "Get all movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movies found, returns a list of movies",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovieResponse.class),
                            examples = @ExampleObject(value = "{\n    \"message\": \"Movies found\",\n    \"status\": 200,\n    \"errors\": [],\n    \"data\": [\n        {\n            \"name\": \"Inception\",\n            \"releaseYear\": 2010,\n            \"synopsis\": \"A skilled thief is given a chance at redemption if he can successfully perform inception.\",\n            \"categories\": [\n                \"Science Fiction\"\n            ],\n            \"createdBy\": \"admin@example.com\",\n            \"createdAt\": \"2024-09-04\",\n            \"updatedAt\": null,\n            \"updatedBy\": null,\n            \"deleted\": false,\n            \"deletedAt\": null,\n            \"deletedBy\": null,\n            \"presignedUrl\": \"The movie does not have an image\"\n        }\n    ]\n}"))
            ),
            @ApiResponse(responseCode = "404", description = "No movies found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(value = "{\n    \"message\": \"No movies found\",\n    \"status\": 404,\n    \"errors\": [],\n    \"data\": []\n}"))
            )
    })
    @GetMapping("/search")
    public ResponseBase<List<MovieResponse>> getMovies(
            @Parameter(description = "Keyword to search in movie name and synopsis")
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "releaseYear") String[] sort,
            @RequestParam(defaultValue = "ASC") String direction){

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        List<MovieResponse> movieResponses = movieService.searchMovies(keyword, categoryName, releaseYear, PageRequest.of(page, size, sortDirection, sort));
        if (movieResponses.isEmpty()) {
            return new ResponseBase<>("No movies found", 404, new ArrayList<>(), Optional.empty());
        }
        return new ResponseBase<>("Movies found", 200, new ArrayList<>(), Optional.of(movieResponses));
    }

    @Operation(summary = "Update a movie by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie successfully updated, returns the updated movie",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovieResponse.class),
                            examples = @ExampleObject(value = "{\n    \"message\": \"Movie updated successfully\",\n    \"status\": 200,\n    \"errors\": [],\n    \"data\": {\n        \"name\": \"It 2\",\n        \"releaseYear\": 2012,\n        \"synopsis\": \"Some synopsis\",\n        \"categories\": [\n            \"Horror\",\n            \"Thriller\"\n        ],\n        \"createdBy\": \"admin@example.com\",\n        \"createdAt\": \"2024-09-04\",\n        \"updatedAt\": \"2024-09-04\",\n        \"updatedBy\": \"admin@example.com\",\n        \"deleted\": false,\n        \"deletedAt\": null,\n        \"deletedBy\": null,\n        \"presignedUrl\": \"The movie does not have an image\"\n    }\n}"))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request, missing required fields. Check the response message",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(value = "{\n    \"message\": \"Error updating movie\",\n    \"status\": 400,\n    \"errors\": [\n        \"Movie name already exists\"\n    ],\n    \"data\": null\n}"))
            ),
    })
    @PutMapping("/admin/update")
    public ResponseBase<MovieResponse> updateMovie(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New data for the movie. The token must be provided in the header", required = true)
            @RequestBody BaseMovieRequest movieUpdateRequest,
            @Parameter(description = "Movie name that will be updated")
            @RequestParam String movieName) {
        return movieService.updateMovie(movieUpdateRequest, movieName);
    }

    @Operation(summary = "Delete a movie by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie successfully deleted, returns true",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(value = "{\n    \"message\": \"Movie deleted successfully\",\n    \"status\": 200,\n    \"errors\": [],\n    \"data\": true\n}"))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request, missing required fields. Check the response message",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBase.class),
                            examples = @ExampleObject(value = "{\n    \"message\": \"Error deleting movie\",\n    \"status\": 400,\n    \"errors\": [\n        \"Movie not found\"\n    ],\n    \"data\": false\n}"))
            )
    })
    @DeleteMapping("/admin/delete")
    public ResponseBase<Boolean> deleteMovie(
            @Parameter(description = "Movie name that will be deleted")
            @RequestParam String movieName) {
        return movieService.deleteMovie(movieName);
    }

    @PostMapping("/admin/upload")
    public ResponseBase<String> uploadImage(@RequestParam MultipartFile file, @RequestParam String movieName) {
        return movieService.uploadImage(movieName, file);
    }
}
