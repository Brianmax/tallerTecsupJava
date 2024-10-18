package org.ravn.moviescatalogchallenge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.ravn.moviescatalogchallenge.aggregate.request.BaseMovieRequest;
import org.ravn.moviescatalogchallenge.aggregate.response.MovieResponse;
import org.ravn.moviescatalogchallenge.entity.Category;
import org.ravn.moviescatalogchallenge.entity.Movie;
import org.ravn.moviescatalogchallenge.entity.UserEntity;

import java.sql.Date;
import java.util.List;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    @Mapping(target="name", source = "movieCreateRequest.name")
    @Mapping(target = "releaseYear", source = "movieCreateRequest.releaseYear")
    @Mapping(target = "synopsis", source = "movieCreateRequest.synopsis")
    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "userEntity", source = "userEntity")
    @Mapping(target = "createdAt", source = "createdAt")
    Movie movieRequestToMovie(
            BaseMovieRequest movieCreateRequest,
            List<Category> categories,
            UserEntity userEntity,
            Date createdAt);
    @Mapping(target = "name", source = "movie.name")
    @Mapping(target = "releaseYear", source = "movie.releaseYear")
    @Mapping(target = "synopsis", source = "movie.synopsis")
    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "createdBy", source = "user")
    @Mapping(target = "createdAt", source = "movie.createdAt")
    @Mapping(target = "updatedAt", source = "movie.updatedAt")
    @Mapping(target = "updatedBy", source = "movie.updatedBy")
    @Mapping(target = "presignedUrl", source = "presignedUrl")
    MovieResponse movieToMovieResponse(
            Movie movie,
            List<String> categories,
            String user,
            String presignedUrl
    );


    @Mapping(target = "name", source = "movieUpdateRequest.name")
    @Mapping(target = "releaseYear", source = "movieUpdateRequest.releaseYear")
    @Mapping(target = "synopsis", source = "movieUpdateRequest.synopsis")
    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "updatedBy", source = "updatedBy")
    void updateMovieFromMovieUpdateRequest(BaseMovieRequest movieUpdateRequest,
                                           @MappingTarget Movie movie,
                                           List<Category> categories,
                                           Date updatedAt,
                                           String updatedBy);
}
