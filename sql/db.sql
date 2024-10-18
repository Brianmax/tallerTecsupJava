CREATE TABLE role (
                      role_id SERIAL PRIMARY KEY,
                      role VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE category (
                          category_id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_entity (
                             user_id SERIAL PRIMARY KEY,
                             email VARCHAR(255) NOT NULL UNIQUE,
                             password VARCHAR(255) NOT NULL,
                             role_id INT REFERENCES role(role_id)
);

CREATE TABLE movies (
                       movie_id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE,
                       release_year INT NOT NULL,
                       synopsis TEXT,
                       poster VARCHAR(255),
                       created_at DATE,
                       updated_at DATE,
                       updated_by VARCHAR(255),
                       user_id INT REFERENCES user_entity(user_id)
);

CREATE TABLE rate (
                      rate_id SERIAL PRIMARY KEY,
                      rate FLOAT NOT NULL,
                      movie_id INT REFERENCES movies(movie_id),
                      user_id INT REFERENCES user_entity(user_id)
);

CREATE TABLE movie_categories (
                                  movie_id INT REFERENCES movies(movie_id),
                                  category_id INT REFERENCES category(category_id)
);

INSERT INTO role (role) VALUES ('USER');
INSERT INTO role (role) VALUES ('ADMIN');

INSERT INTO user_entity (email, password, role_id) VALUES ('user@example.com', 'password123', 1); -- USER role
INSERT INTO user_entity (email, password, role_id) VALUES ('admin@example.com', 'adminpass', 2); -- ADMIN role

INSERT INTO category (name) VALUES ('Action');
INSERT INTO category (name) VALUES ('Comedy');
INSERT INTO category (name) VALUES ('Drama');
INSERT INTO category (name) VALUES ('Horror');
INSERT INTO category (name) VALUES ('Science Fiction');
INSERT INTO category (name) VALUES ('Thriller');

INSERT INTO movies (name, release_year, synopsis, created_at, user_id)
VALUES
    ('Inception', 2010, 'A skilled thief is given a chance at redemption if he can successfully perform inception.', NOW(), 2),
    ('The Matrix', 1999, 'A hacker discovers the truth about his reality and his role in the war against its controllers.', NOW(), 2),
    ('Interstellar', 2014, 'A team of explorers travel through a wormhole in space in an attempt to ensure humanitys survival.', NOW(), 2),
('The Godfather', 1972, 'The aging patriarch of an organized crime dynasty transfers control of his empire to his reluctant son.', NOW(), 2),
('The Dark Knight', 2008, 'Batman battles the Joker, who plunges Gotham City into anarchy.', NOW(), 2);

INSERT INTO movie_categories (movie_id, category_id) VALUES (1, 5); -- Inception -> Science Fiction
INSERT INTO movie_categories (movie_id, category_id) VALUES (2, 5); -- The Matrix -> Science Fiction
INSERT INTO movie_categories (movie_id, category_id) VALUES (3, 5); -- Interstellar -> Science Fiction
INSERT INTO movie_categories (movie_id, category_id) VALUES (4, 3); -- The Godfather -> Drama
INSERT INTO movie_categories (movie_id, category_id) VALUES (5, 1); -- The Dark Knight -> Action
