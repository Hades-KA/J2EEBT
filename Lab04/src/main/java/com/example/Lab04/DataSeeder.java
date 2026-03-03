package com.example.Lab04;

import com.example.Lab04.model.Category;
import com.example.Lab04.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedCategories(CategoryRepository categoryRepo) {
        return args -> {
            if (categoryRepo.count() == 0) {
                categoryRepo.save(new Category(null, "Laptop"));
                categoryRepo.save(new Category(null, "Điện thoại"));
                categoryRepo.save(new Category(null, "Phụ kiện"));
            }
        };
    }
}