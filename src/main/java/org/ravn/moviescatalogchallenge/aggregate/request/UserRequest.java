package org.ravn.moviescatalogchallenge.aggregate.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

@Getter
@Setter
public class UserRequest {
    @Email
    private String email;
    @Min(value = 6, message = "The password must contain at least 6 character")
    private String password;
    private String role;
}
