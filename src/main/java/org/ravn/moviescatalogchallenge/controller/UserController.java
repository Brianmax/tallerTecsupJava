package org.ravn.moviescatalogchallenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ravn.moviescatalogchallenge.aggregate.request.LoginRequest;
import org.ravn.moviescatalogchallenge.aggregate.request.UserRequest;
import org.ravn.moviescatalogchallenge.aggregate.response.ResponseBase;
import org.ravn.moviescatalogchallenge.aggregate.response.UserResponse;
import org.ravn.moviescatalogchallenge.service.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
@Tag(name = "User", description = "Create a new user and login")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created, returns the user with his role",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class),
                    examples = @ExampleObject(value = "{\n" +
                            "  \"email\": \"user@gmail.com\",\n" +
                            "  \"role\": \"USER\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "400", description = "Invalid request, missing required fields. Check the response message",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBase.class),
                    examples = @ExampleObject(value = "{\n    \"message\": \"Error creating user\",\n    \"status\": 400,\n    \"errors\": [\n        \"User already exists\"\n    ],\n    \"data\": null\n}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))})
    @PostMapping("auth/signup")
    public ResponseBase<UserResponse> saveUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User email and password, The password must be at least 6 characters long", required = true)
            @RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in, returns the token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBase.class),
                    examples = @ExampleObject(value = "{\n    \"message\": \"Login successful\",\n    \"status\": 200,\n    \"errors\": [],\n    \"data\": \"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsInN1YiI6InVzZXJAZXhhbXBsZS5jb20iLCJpYXQiOjE3MjU0OTI3MTQsImV4cCI6MTcyNTQ5MzkxNH0.72XmJ5dI57xk_2n4EOY5iJt2c9mdtL8mZ1T6PaOpy-k\"\n}"))),
            @ApiResponse(responseCode = "400", description = "Invalid request, missing required fields. Check the response message",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBase.class),
                    examples = @ExampleObject(value = "{\n    \"message\": \"Login failed\",\n    \"status\": 401,\n    \"errors\": [\n        \"Invalid credentials\"\n    ],\n    \"data\": null\n}"))),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))})
    @PostMapping("auth/login")
    public ResponseBase<String> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Your email and password", required = true)
            @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}
