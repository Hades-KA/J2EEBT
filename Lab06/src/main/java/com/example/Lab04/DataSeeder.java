package com.example.Lab04;

import com.example.Lab04.model.Account;
import com.example.Lab04.model.Category;
import com.example.Lab04.model.Product;
import com.example.Lab04.model.Role;
import com.example.Lab04.repository.AccountRepository;
import com.example.Lab04.repository.CategoryRepository;
import com.example.Lab04.repository.ProductRepository;
import com.example.Lab04.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAll(
            CategoryRepository categoryRepo,
            RoleRepository roleRepo,
            AccountRepository accountRepo,
            ProductRepository productRepo,
            PasswordEncoder encoder
    ) {
        return args -> {

            // ===== Seed categories =====
            Category catLaptop;
            Category catPhone;
            Category catAccessory;

            if (categoryRepo.count() == 0) {
                catLaptop = categoryRepo.save(new Category(null, "Laptop"));
                catPhone = categoryRepo.save(new Category(null, "Điện thoại"));
                catAccessory = categoryRepo.save(new Category(null, "Phụ kiện"));
            } else {
                catLaptop = categoryRepo.findAll().stream()
                        .filter(c -> c.getName().equals("Laptop")).findFirst().orElse(null);
                catPhone = categoryRepo.findAll().stream()
                        .filter(c -> c.getName().equals("Điện thoại")).findFirst().orElse(null);
                catAccessory = categoryRepo.findAll().stream()
                        .filter(c -> c.getName().equals("Phụ kiện")).findFirst().orElse(null);
            }

            // ===== Seed roles =====
            Role adminRole = roleRepo.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepo.save(new Role(null, "ROLE_ADMIN")));

            Role userRole = roleRepo.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepo.save(new Role(null, "ROLE_USER")));

            // ===== Seed accounts =====
            if (accountRepo.findByLoginName("admin").isEmpty()) {
                Account admin = new Account();
                admin.setLoginName("admin");
                admin.setPassword(encoder.encode("123"));
                admin.getRoles().add(adminRole);
                accountRepo.save(admin);
            }

            if (accountRepo.findByLoginName("user1").isEmpty()) {
                Account user1 = new Account();
                user1.setLoginName("user1");
                user1.setPassword(encoder.encode("123"));
                user1.getRoles().add(userRole);
                accountRepo.save(user1);
            }

            // ===== Seed 20 sản phẩm có ảnh =====
            if (productRepo.count() == 0) {

                // --- LAPTOP (7 sản phẩm) ---
                productRepo.save(new Product(null, "MacBook Air M2",
                        "https://picsum.photos/seed/macbook-air/200/200",
                        2899000L, catLaptop));

                productRepo.save(new Product(null, "MacBook Pro M3",
                        "https://picsum.photos/seed/macbook-pro/200/200",
                        3999000L, catLaptop));

                productRepo.save(new Product(null, "Dell XPS 15",
                        "https://picsum.photos/seed/dell-xps/200/200",
                        3200000L, catLaptop));

                productRepo.save(new Product(null, "Asus ROG Strix G16",
                        "https://picsum.photos/seed/asus-rog/200/200",
                        3550000L, catLaptop));

                productRepo.save(new Product(null, "HP Pavilion 15",
                        "https://picsum.photos/seed/hp-pavilion/200/200",
                        1899000L, catLaptop));

                productRepo.save(new Product(null, "Lenovo ThinkPad X1",
                        "https://picsum.photos/seed/lenovo-x1/200/200",
                        3450000L, catLaptop));

                productRepo.save(new Product(null, "Acer Nitro 5",
                        "https://picsum.photos/seed/acer-nitro/200/200",
                        2199000L, catLaptop));

                // --- ĐIỆN THOẠI (7 sản phẩm) ---
                productRepo.save(new Product(null, "iPhone 15 Pro Max",
                        "https://picsum.photos/seed/iphone15/200/200",
                        2999000L, catPhone));

                productRepo.save(new Product(null, "Samsung Galaxy S24 Ultra",
                        "https://picsum.photos/seed/samsung-s24/200/200",
                        2799000L, catPhone));

                productRepo.save(new Product(null, "Xiaomi 14 Ultra",
                        "https://picsum.photos/seed/xiaomi14/200/200",
                        1999000L, catPhone));

                productRepo.save(new Product(null, "Google Pixel 8 Pro",
                        "https://picsum.photos/seed/pixel8/200/200",
                        2499000L, catPhone));

                productRepo.save(new Product(null, "OPPO Find X7 Ultra",
                        "https://picsum.photos/seed/oppo-x7/200/200",
                        2299000L, catPhone));

                productRepo.save(new Product(null, "Huawei P60 Pro",
                        "https://picsum.photos/seed/huawei-p60/200/200",
                        1899000L, catPhone));

                productRepo.save(new Product(null, "OnePlus 12",
                        "https://picsum.photos/seed/oneplus12/200/200",
                        1799000L, catPhone));

                // --- PHỤ KIỆN (6 sản phẩm) ---
                productRepo.save(new Product(null, "AirPods Pro 2",
                        "https://picsum.photos/seed/airpods/200/200",
                        599000L, catAccessory));

                productRepo.save(new Product(null, "Chuột Logitech MX Master 3S",
                        "https://picsum.photos/seed/logitech-mx/200/200",
                        249000L, catAccessory));

                productRepo.save(new Product(null, "Bàn phím cơ Keychron K8",
                        "https://picsum.photos/seed/keychron/200/200",
                        199000L, catAccessory));

                productRepo.save(new Product(null, "Tai nghe Sony WH-1000XM5",
                        "https://picsum.photos/seed/sony-xm5/200/200",
                        799000L, catAccessory));

                productRepo.save(new Product(null, "Sạc Apple MagSafe",
                        "https://picsum.photos/seed/magsafe/200/200",
                        149000L, catAccessory));

                productRepo.save(new Product(null, "Ốp lưng Samsung Clear Case",
                        "https://picsum.photos/seed/samsung-case/200/200",
                        99000L, catAccessory));
            }
        };
    }
}