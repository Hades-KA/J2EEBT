package com.example.Lab04.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.Lab04.model.Category;
import com.example.Lab04.model.Product;
import com.example.Lab04.service.CategoryService;
import com.example.Lab04.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("listProduct", productService.getAll());
        return "product/products";
    }

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

        Category selected = categoryService.getById(categoryId);
        editProduct.setCategory(selected);

        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(editProduct, imageProduct);
        }
        productService.update(editProduct);

        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }
}