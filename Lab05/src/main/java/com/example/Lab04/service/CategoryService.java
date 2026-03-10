package com.example.Lab04.service;

import com.example.Lab04.model.Category;
import com.example.Lab04.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> getAll() {
        return repo.findAll();
    }

    public Category getById(int id) {
        return repo.findById(id).orElse(null);
    }
}