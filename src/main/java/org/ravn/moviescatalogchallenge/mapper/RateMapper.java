package org.ravn.moviescatalogchallenge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.ravn.moviescatalogchallenge.aggregate.request.RateDto;
import org.ravn.moviescatalogchallenge.entity.Movie;
import org.ravn.moviescatalogchallenge.entity.Rate;
import org.ravn.moviescatalogchallenge.entity.UserEntity;

@Mapper
public interface RateMapper {

    RateMapper INSTANCE = Mappers.getMapper(RateMapper.class);

    @Mapping(target = "rateId", ignore = true)
    @Mapping(target = "movie", source = "movie")
    @Mapping(target = "userEntity", source = "userEntity")
    @Mapping(target = "rate", source = "rateDto.rate")
    Rate rateDtoToRate(RateDto rateDto, Movie movie, UserEntity userEntity);


    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "movieName", source = "movie.name")
    RateDto rateToRateDto(Rate rate);
}
