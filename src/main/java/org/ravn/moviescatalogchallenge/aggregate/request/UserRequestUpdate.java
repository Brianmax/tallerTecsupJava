package org.ravn.moviescatalogchallenge.aggregate.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestUpdate {
    private String password;
    private String role;
}
