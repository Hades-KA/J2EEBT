package com.example.Lab04.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.Lab04.model.Product;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class ProductService {
    private final List<Product> listProduct = new ArrayList<>();

    public List<Product> getAll() {
        return listProduct;
    }

    public Product get(int id) {
        return listProduct.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public void add(Product newProduct) {
        int maxId = listProduct.stream().mapToInt(Product::getId).max().orElse(0);
        newProduct.setId(maxId + 1);
        listProduct.add(newProduct);
    }

    public void update(Product editProduct) {
        Product found = get(editProduct.getId());
        if (found != null) {
            found.setName(editProduct.getName());
            found.setPrice(editProduct.getPrice());
            found.setCategory(editProduct.getCategory());
            if (editProduct.getImage() != null) found.setImage(editProduct.getImage());
        }
    }

    public void delete(int id) {
        listProduct.removeIf(p -> p.getId() == id);
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