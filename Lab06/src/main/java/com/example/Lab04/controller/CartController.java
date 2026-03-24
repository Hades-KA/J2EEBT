package com.example.Lab04.controller;

import com.example.Lab04.model.Order;
import com.example.Lab04.service.CartService;
import com.example.Lab04.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    // Hiển thị giỏ hàng
    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("total", cartService.getTotal());
        return "cart/list";
    }

    // Thêm sản phẩm vào giỏ
    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable int id) {
        cartService.addToCart(id);
        return "redirect:/cart";
    }

    // Cập nhật số lượng
    @PostMapping("/update")
    public String updateQuantity(@RequestParam int productId,
                                 @RequestParam int quantity) {
        cartService.updateQuantity(productId, quantity);
        return "redirect:/cart";
    }

    // Xóa 1 sản phẩm khỏi giỏ
    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable int id) {
        cartService.removeFromCart(id);
        return "redirect:/cart";
    }

    // Xóa toàn bộ giỏ hàng
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clear();
        return "redirect:/cart";
    }

    // ===== ĐẶT HÀNG (CHECKOUT) =====
    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        // Kiểm tra giỏ hàng trống
        if (cartService.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        // Tạo đơn hàng
        String customerName = userDetails.getUsername();
        Order order = orderService.createOrder(customerName, cartService.getItems());

        // Xóa giỏ hàng sau khi đặt
        cartService.clear();

        model.addAttribute("order", order);
        return "cart/checkout-success";
    }
}