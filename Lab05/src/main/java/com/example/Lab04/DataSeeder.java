package com.example.Lab04;

import com.example.Lab04.model.Account;
import com.example.Lab04.model.Category;
import com.example.Lab04.model.Role;
import com.example.Lab04.repository.AccountRepository;
import com.example.Lab04.repository.CategoryRepository;
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
            PasswordEncoder encoder
    ) {
        return args -> {

            // ===== Seed categories =====
            if (categoryRepo.count() == 0) {
                categoryRepo.save(new Category(null, "Laptop"));
                categoryRepo.save(new Category(null, "Điện thoại"));
                categoryRepo.save(new Category(null, "Phụ kiện"));
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
                admin.setPassword(encoder.encode("123")); // mật khẩu: 123
                admin.getRoles().add(adminRole);
                accountRepo.save(admin);
            }

            if (accountRepo.findByLoginName("user1").isEmpty()) {
                Account user1 = new Account();
                user1.setLoginName("user1");
                user1.setPassword(encoder.encode("123")); // mật khẩu: 123
                user1.getRoles().add(userRole);
                accountRepo.save(user1);
            }
        };
    }
}