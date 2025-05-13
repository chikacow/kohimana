package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM tbl_product WHERE code = :code",
            nativeQuery = true)
    public Optional<Product> findByCode(@Param("code") String code);

    @Query(value = "SELECT * from tbl_product where id = :id LIMIT 1", nativeQuery = true)
    public Optional<Product> findById(@Param("id") Long id);


}
