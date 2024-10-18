package org.ravn.moviescatalogchallenge.repository;

import org.ravn.moviescatalogchallenge.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.name IN :names")
    List<Category> findByNames(List<String> names);
}
