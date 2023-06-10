package com.ape.repository;

import com.ape.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {

    boolean existsByName(String name);

    @Query("SELECT count(b) FROM Brand b JOIN b.image img WHERE img.id=:id")
    Integer findBrandsByImageId(@Param("id") String imageId);

    boolean existsByImageId(String image);
}
