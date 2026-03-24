package com.example.Lab04.service;

import com.example.Lab04.model.CartItem;
import com.example.Lab04.model.Order;
import com.example.Lab04.model.OrderDetail;
import com.example.Lab04.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(String customerName, List<CartItem> cartItems) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setOrderDate(LocalDateTime.now());

        double total = 0;

        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setProductName(item.getName());
            detail.setPrice(item.getPrice());
            detail.setQuantity(item.getQuantity());
            detail.setSubtotal(item.getPrice() * item.getQuantity());
            detail.setOrder(order);

            order.getOrderDetails().add(detail);
            total += detail.getSubtotal();
        }

        order.setTotalPrice(total);

        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}