package org.ravn.moviescatalogchallenge.aggregate.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BaseMovieRequest {
    protected String name;
    protected int releaseYear;
    protected String synopsis;
    protected List<String> categories;
}
