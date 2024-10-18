package org.ravn.moviescatalogchallenge.aggregate.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateDto {
    protected float rate;
    protected String movieName;
}
