package com.ape.repository;

import com.ape.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query( "SELECT p FROM Product p JOIN p.image img WHERE img.id=:id")
    Product findProductByImageId(@Param("id") String id );
}
