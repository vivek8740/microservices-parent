package com.springapp.prod.repository;


import com.springapp.prod.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product,String> {
}
