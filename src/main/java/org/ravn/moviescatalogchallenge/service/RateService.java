package org.ravn.moviescatalogchallenge.service;

import org.ravn.moviescatalogchallenge.aggregate.request.RateDto;
import org.ravn.moviescatalogchallenge.aggregate.response.ResponseBase;

import java.util.List;

public interface RateService {
    ResponseBase<RateDto> rateMovie(RateDto rateDto);
    List<RateDto> getMovieRates();
    ResponseBase<Boolean> deleteRate(String movieName);
}
