package org.ravn.moviescatalogchallenge.aggregate.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBase<T> {
    private String message;
    private int status;
    private List <String> errors;
    Optional <T> data;
}