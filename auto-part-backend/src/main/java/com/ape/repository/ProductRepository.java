package com.ape.repository;

import com.ape.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query( "SELECT p FROM Product p JOIN p.image img WHERE img.id=:id")
    Product findProductByImageId(@Param("id") String id );

    @EntityGraph(attributePaths = {"image","id"})
    Optional<Product> findProductById(Long id);

    Boolean existsByBrandId(Long id);

    Boolean existsByCategoryId(Long id);

    @Query("SELECT p FROM OrderItem oi INNER JOIN oi.product p WHERE p.id=:productId")
    List<Product> checkOrderItemsByID(@Param("productId") Long id);
}
