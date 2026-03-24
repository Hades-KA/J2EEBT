package com.example.Lab04.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    // Product info
    private Integer id;
    private String name;
    private String image;
    private long price;

    // Quantity
    private int quantity;
}