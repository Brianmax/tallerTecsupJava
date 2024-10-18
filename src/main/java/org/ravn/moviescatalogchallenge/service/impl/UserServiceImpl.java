package org.ravn.moviescatalogchallenge.service.impl;

import jdk.jshell.execution.Util;
import org.ravn.moviescatalogchallenge.aggregate.request.LoginRequest;
import org.ravn.moviescatalogchallenge.aggregate.request.UserRequest;
import org.ravn.moviescatalogchallenge.aggregate.request.UserRequestUpdate;
import org.ravn.moviescatalogchallenge.aggregate.response.ResponseBase;
import org.ravn.moviescatalogchallenge.aggregate.response.UserResponse;
import org.ravn.moviescatalogchallenge.entity.Role;
import org.ravn.moviescatalogchallenge.entity.UserEntity;
import org.ravn.moviescatalogchallenge.mapper.UserMapper;
import org.ravn.moviescatalogchallenge.repository.RoleRepository;
import org.ravn.moviescatalogchallenge.repository.UserRepository;
import org.ravn.moviescatalogchallenge.security.UserDetailsServiceImpl;
import org.ravn.moviescatalogchallenge.service.JwtService;
import org.ravn.moviescatalogchallenge.service.UserService;
import org.ravn.moviescatalogchallenge.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    public ResponseBase<UserResponse> createUser(UserRequest userRequest) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(userRequest.getEmail());
        Optional<Role> roleOptional = roleRepository.findByRole(userRequest.getRole());
        List<String> errors = validateInput(userRequest);
        if (userOptional.isPresent()) {
            errors.add("User already exists");
        }
        if (roleOptional.isEmpty()) {
            errors.add("Role not found");
        }
        if (!errors.isEmpty()) {
            return new ResponseBase<>(
                    "Error creating user",
                    400,
                    errors,
                    Optional.empty());
        }

        UserEntity userEntity = UserMapper.INSTANCE.userRequestToUser(userRequest, roleOptional.get());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(userRequest.getPassword()));
        userRepository.save(userEntity);
        return new ResponseBase<>(
                "User created",
                200,
                new ArrayList<>(),
                Optional.of(UserMapper.INSTANCE.userToUserResponse(userEntity)));
    }

    @Override
    public Optional<UserResponse> updateUser(UserRequestUpdate userRequestUpdate, int id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        Optional<Role> roleOptional = roleRepository.findByRole(userRequestUpdate.getRole());

        if (userOptional.isEmpty() || roleOptional.isEmpty()) {
            return Optional.empty();
        }
        UserEntity userEntity = UserMapper.INSTANCE.userRequestUpdateToUser(
                userRequestUpdate,
                roleOptional.get(),
                userOptional.get()
        );
        userRepository.save(userEntity);
        return Optional.of(UserMapper.INSTANCE.userToUserResponse(userEntity));
    }

    @Override
    public ResponseBase<String> login(LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
            if (auth.isAuthenticated()) {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginRequest.getEmail());
                String jwt = jwtService.generateToken(userDetails);
                return new ResponseBase<>(
                        "Login successful",
                        200,
                        new ArrayList<>(),
                        Optional.of(jwt)
                );
            }
        } catch (Exception e) {
            String errorMessage = "Error login: " + e.getMessage();
            logger.error(errorMessage);
        }

        return new ResponseBase<>(
                "Login failed",
                401,
                List.of("Invalid credentials"),
                Optional.empty()
        );
    }

    private List<String> validateInput(UserRequest userRequest) {
        List<String> errors = new ArrayList<>();
        if (userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
            errors.add("Email is required");
        }
        // verify if email is valid

        if (!Utils.isValidEmail(userRequest.getEmail())) {
            errors.add("Invalid email");
        }

        if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
            errors.add("Password is required");
        }
        if (userRequest.getRole() == null || userRequest.getRole().isEmpty()) {
            errors.add("Role is required");
        }
        return errors;
    }
}