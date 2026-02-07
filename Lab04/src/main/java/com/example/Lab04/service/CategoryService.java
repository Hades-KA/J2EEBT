package com.example.Lab04.service;

import org.springframework.stereotype.Service;
import com.example.Lab04.model.Category;

import java.util.List;

@Service
public class CategoryService {
    private final List<Category> categories = List.of(
            new Category(1, "Laptop"),
            new Category(2, "Điện thoại"),
            new Category(3, "Phụ kiện")
    );

    public List<Category> getAll() {
        return categories;
    }

    public Category getById(int id) {
        return categories.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
}