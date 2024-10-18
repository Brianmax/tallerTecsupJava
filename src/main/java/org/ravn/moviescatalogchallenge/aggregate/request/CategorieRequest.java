package org.ravn.moviescatalogchallenge.aggregate.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorieRequest {
    private String name;
    private String description;
}
