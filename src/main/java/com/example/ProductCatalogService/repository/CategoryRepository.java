package com.example.ProductCatalogService.repository;

import com.example.ProductCatalogService.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);

    @Query(value = "SELECT AVG(p.price) from Product p where p.category.name=:categoryName")
    Double findAvgPriceForCategory(@Param("categoryName") String category);

}
