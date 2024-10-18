package org.ravn.moviescatalogchallenge.config;

import org.apache.catalina.User;
import org.ravn.moviescatalogchallenge.entity.Category;
import org.ravn.moviescatalogchallenge.entity.Movie;
import org.ravn.moviescatalogchallenge.entity.Role;
import org.ravn.moviescatalogchallenge.entity.UserEntity;
import org.ravn.moviescatalogchallenge.repository.CategoriesRepository;
import org.ravn.moviescatalogchallenge.repository.MovieRepository;
import org.ravn.moviescatalogchallenge.repository.RoleRepository;
import org.ravn.moviescatalogchallenge.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class IntialConfig {
    private final UserRepository userRepository;

    public IntialConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {

            UserEntity user = userRepository.findByEmail("user@example.com").orElse(null);
            UserEntity admin = userRepository.findByEmail("admin@example.com").orElse(null);

            if (user != null){
                user.setPassword(new BCryptPasswordEncoder().encode("password"));
                userRepository.save(user);
            }
            if (admin != null){
                admin.setPassword(new BCryptPasswordEncoder().encode("password"));
                userRepository.save(admin);
            }
        };
    }

}
