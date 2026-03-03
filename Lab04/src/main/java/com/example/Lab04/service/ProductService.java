package com.example.Lab04.service;

import com.example.Lab04.model.Product;
import com.example.Lab04.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product get(int id) {
        return repo.findById(id).orElse(null);
    }

    public void add(Product newProduct) {
        repo.save(newProduct);
    }

    public void update(Product editProduct) {
        repo.save(editProduct);
    }

    public void delete(int id) {
        repo.deleteById(id);
    }

    public void updateImage(Product product, MultipartFile imageProduct) {
        if (imageProduct == null || imageProduct.isEmpty()) return;

        try {
            String contentType = imageProduct.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Tệp tải lên không phải hình ảnh!");
            }

            Path dirImages = Paths.get("uploads/images");
            if (!Files.exists(dirImages)) Files.createDirectories(dirImages);

            String newFileName = UUID.randomUUID() + "_" + imageProduct.getOriginalFilename();
            Path pathFileUpload = dirImages.resolve(newFileName);

            Files.copy(imageProduct.getInputStream(), pathFileUpload, StandardCopyOption.REPLACE_EXISTING);

            product.setImage(newFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}