package com.example.Lab04.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles") // đổi từ "role" -> "roles" để tránh keyword MySQL
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Spring Security yêu cầu prefix ROLE_
    @Column(nullable = false, unique = true, length = 50)
    private String name; // ROLE_ADMIN, ROLE_USER
}