package com.example.Lab04.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Column(nullable = false)
    private String name;
}