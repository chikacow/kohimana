package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Product, Long> {
}
