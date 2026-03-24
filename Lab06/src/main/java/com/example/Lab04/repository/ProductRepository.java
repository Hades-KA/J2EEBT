package com.example.Lab04.repository;

import com.example.Lab04.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Tìm kiếm theo tên
    List<Product> findByNameContainingIgnoreCase(String name);

    // Phân trang
    Page<Product> findAll(Pageable pageable);

    // Lọc theo category + phân trang
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
}