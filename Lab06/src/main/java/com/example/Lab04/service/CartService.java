package com.example.Lab04.service;

import com.example.Lab04.model.CartItem;
import com.example.Lab04.model.Product;
import com.example.Lab04.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CartService {

    private List<CartItem> items = new ArrayList<>();

    @Autowired
    ProductRepository productRepository;

    public List<CartItem> getItems() {
        return items;
    }

    public void addToCart(int productId) {
        // Tìm sản phẩm từ database
        Product findProduct = productRepository.findById(productId).orElse(null);
        if (findProduct == null) {
            return; // Không tìm thấy sản phẩm
        }

        // Kiểm tra sản phẩm đã có trong giỏ chưa
        // đã có -> tăng số lượng
        // chưa có -> thêm mới với quantity = 1
        items.stream()
                .filter(item -> item.getId().equals(findProduct.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + 1),
                        () -> {
                            CartItem newItem = new CartItem();
                            newItem.setId(productId);
                            newItem.setName(findProduct.getName());
                            newItem.setImage(findProduct.getImage());
                            newItem.setPrice(findProduct.getPrice());
                            newItem.setQuantity(1);
                            items.add(newItem);
                        }
                );
    }

    public void updateQuantity(int productId, int quantity) {
        // Tìm sản phẩm trong giỏ hàng để cập nhật số lượng
        items.stream()
                .filter(item -> item.getId() == productId)
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
    }

    public void removeFromCart(int productId) {
        items.removeIf(item -> item.getId().equals(productId));
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}