package org.ravn.moviescatalogchallenge.service.impl;

import org.ravn.moviescatalogchallenge.aggregate.request.RateDto;
import org.ravn.moviescatalogchallenge.aggregate.response.ResponseBase;
import org.ravn.moviescatalogchallenge.entity.Movie;
import org.ravn.moviescatalogchallenge.entity.Rate;
import org.ravn.moviescatalogchallenge.entity.UserEntity;
import org.ravn.moviescatalogchallenge.mapper.RateMapper;
import org.ravn.moviescatalogchallenge.repository.MovieRepository;
import org.ravn.moviescatalogchallenge.repository.RateRepository;
import org.ravn.moviescatalogchallenge.repository.UserRepository;
import org.ravn.moviescatalogchallenge.service.RateService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.ravn.moviescatalogchallenge.utils.Utils.getLoggedUser;

@Service
public class RateServiceImpl implements RateService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final RateRepository rateRepository;
    private final HttpServletRequest request;

    public RateServiceImpl(UserRepository userRepository, MovieRepository movieRepository, RateRepository rateRepository, HttpServletRequest request) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.rateRepository = rateRepository;
        this.request = request;
    }


    @Override
    public ResponseBase<RateDto> rateMovie(RateDto rateDto) {
        String userLogged = getLoggedUser(request);
        List<String> errors = validateRate(rateDto);
        if (!errors.isEmpty()) {
            return new ResponseBase<>("Input validation failed", 400, errors, null);
        }
        Optional<UserEntity> user = userRepository.findByEmail(userLogged);
        Optional<Movie> movie = movieRepository.findByName(rateDto.getMovieName());
        if (user.isEmpty()) {
            errors.add("User not found");
        }
        if (movie.isEmpty()) {
            errors.add("Movie not found");
        }
        if (!errors.isEmpty()) {
            return new ResponseBase<>("Rate validation failed", 400, errors, null);
        }

        Movie movieBd = movie.get();
        UserEntity userEntity = user.get();
        Optional<Rate> rateOptional = rateRepository.findByMovieNameAndUserEntityEmail(rateDto.getMovieName(), userLogged);

        if ( rateOptional.isPresent() ) {
            return new ResponseBase<>("Rate already exists", 400, List.of("Rate already exists"),
                    Optional.of(RateMapper.INSTANCE.rateToRateDto(rateOptional.get()))
            );
        }
        Rate rate = RateMapper.INSTANCE.rateDtoToRate(rateDto, movieBd, userEntity);
        rateRepository.save(rate);

        return new ResponseBase<>("Rate saved", 200, new ArrayList<>(),
                Optional.of(RateMapper.INSTANCE.rateToRateDto(rate))
        );
    }

    @Override
    public List<RateDto> getMovieRates() {
        String user = getLoggedUser(request);
        List<Rate> rates = rateRepository.findByUserEntityEmail(user);
        return rates.stream().map(RateMapper.INSTANCE::rateToRateDto).collect(Collectors.toList());
    }

    @Override
    public ResponseBase<Boolean> deleteRate(String movieName) {
        String user = getLoggedUser(request);
        Optional<Rate> rate = rateRepository.findByMovieNameAndUserEntityEmail(movieName, user);
        if (rate.isEmpty()) {
            return new ResponseBase<>("Rate not found", 404, List.of("Rate not found"), Optional.of(false));
        }
        rateRepository.delete(rate.get());
        return new ResponseBase<>("Rate deleted", 200, new ArrayList<>(), Optional.of(true));
    }

    private List<String> validateRate(RateDto rateDto) {
        List<String> errors = new ArrayList<>();
        if (rateDto.getRate() < 0 || rateDto.getRate() > 5) {
            errors.add("Rate must be between 0 and 5");
        }
        if (rateDto.getMovieName() == null || rateDto.getMovieName().isEmpty()) {
            errors.add("Movie name must not be empty");
        }
        return errors;
    }
}
