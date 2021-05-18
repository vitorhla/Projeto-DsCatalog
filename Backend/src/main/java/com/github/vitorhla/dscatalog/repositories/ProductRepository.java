package com.github.vitorhla.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.vitorhla.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
