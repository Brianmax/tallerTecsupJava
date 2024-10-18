package org.ravn.moviescatalogchallenge.repository.specification;

import org.ravn.moviescatalogchallenge.entity.Category;
import org.ravn.moviescatalogchallenge.entity.Movie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import java.util.List;

public class MovieSpecification {
    public static Specification<Movie> hasNameOrSynopsis(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("synopsis")), likePattern)
            );
        };
    }

    public static Specification<Movie> hasCategoryName(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Movie, Category> categoryJoin = root.join("categories");
            return criteriaBuilder.equal(criteriaBuilder.lower(categoryJoin.get("name")), categoryName.toLowerCase());
        };
    }


    public static Specification<Movie> releasedInYear(Integer year) {
        return (root, query, criteriaBuilder) -> {
            if (year == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("releaseYear"), year);
        };
    }
}
