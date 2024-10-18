package org.ravn.moviescatalogchallenge.service;

import org.ravn.moviescatalogchallenge.aggregate.request.BaseMovieRequest;
import org.ravn.moviescatalogchallenge.aggregate.response.MovieResponse;
import org.ravn.moviescatalogchallenge.aggregate.response.ResponseBase;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public interface MovieService {
    ResponseBase<MovieResponse> createMovie(BaseMovieRequest movieCreateRequest);
    List<MovieResponse> getAllMovies(int page, int size);
    ResponseBase<MovieResponse> updateMovie(BaseMovieRequest movieUpdateRequest, String movieName);
    ResponseBase<Boolean> deleteMovie(String movieName);
    List<MovieResponse> searchMovies(String keyword, String categoryName, Integer releaseYear, Pageable pageable);
    ResponseBase<String> uploadImage(String movieName, MultipartFile file);
}
