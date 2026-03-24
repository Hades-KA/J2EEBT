package com.example.Lab04.controller;

import com.example.Lab04.model.Category;
import com.example.Lab04.model.Product;
import com.example.Lab04.service.CategoryService;
import com.example.Lab04.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;

    // ===== DANH SÁCH + PHÂN TRANG + SẮP XẾP + LỌC CATEGORY =====
    @GetMapping
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "") String sort,
                        @RequestParam(required = false) Integer categoryId) {

        Page<Product> productPage;
        Sort sortOrder = Sort.unsorted();

        // Xác định thứ tự sắp xếp
        switch (sort) {
            case "price_asc":
                sortOrder = Sort.by("price").ascending();
                break;
            case "price_desc":
                sortOrder = Sort.by("price").descending();
                break;
        }

        // Lọc theo category hoặc lấy tất cả
        if (categoryId != null) {
            if (sortOrder.isSorted()) {
                productPage = productService.getProductsByCategoryAndPage(categoryId, page, 5, sortOrder);
            } else {
                productPage = productService.getProductsByCategoryAndPage(categoryId, page, 5);
            }
        } else {
            if (sortOrder.isSorted()) {
                productPage = productService.getProductsByPage(page, 5, sortOrder);
            } else {
                productPage = productService.getProductsByPage(page, 5);
            }
        }

        model.addAttribute("listProduct", productPage);
        model.addAttribute("currentSort", sort);
        model.addAttribute("currentCategoryId", categoryId);
        model.addAttribute("categories", categoryService.getAll());
        return "product/products";
    }

    // ===== TÌM KIẾM =====
    @GetMapping("/search")
    public String search(Model model, @RequestParam("key") String key) {
        List<Product> listProduct = productService.getSearchProducts(key);
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("searchKey", key);
        return "product/search-results";
    }

    // ===== TẠO SẢN PHẨM =====
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }

    @PostMapping("/create")
    public String createPost(
            @Valid @ModelAttribute("product") Product newProduct,
            BindingResult result,
            @RequestParam("category.id") int categoryId,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        Category selected = categoryService.getById(categoryId);
        newProduct.setCategory(selected);

        productService.updateImage(newProduct, imageProduct);
        productService.add(newProduct);

        return "redirect:/products";
    }

    // ===== SỬA SẢN PHẨM =====
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product found = productService.get(id);
        if (found == null) return "error/404";

        model.addAttribute("product", found);
        model.addAttribute("categories", categoryService.getAll());
        return "product/edit";
    }

    @PostMapping("/edit")
    public String editPost(
            @Valid @ModelAttribute("product") Product editProduct,
            BindingResult result,
            @RequestParam("category.id") int categoryId,
            @RequestParam(value = "imageProduct", required = false) MultipartFile imageProduct,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }

        Product old = productService.get(editProduct.getId());
        if (old == null) return "error/404";

        Category selected = categoryService.getById(categoryId);
        editProduct.setCategory(selected);

        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(editProduct, imageProduct);
        } else {
            editProduct.setImage(old.getImage());
        }

        productService.update(editProduct);
        return "redirect:/products";
    }

    // ===== XÓA SẢN PHẨM =====
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }
}