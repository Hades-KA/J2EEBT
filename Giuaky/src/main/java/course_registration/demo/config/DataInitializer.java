package course_registration.demo.config;

import course_registration.demo.entity.*;
import course_registration.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository,
                               StudentRepository studentRepository,
                               CategoryRepository categoryRepository,
                               CourseRepository courseRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(null, RoleName.ADMIN)));

            Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                    .orElseGet(() -> roleRepository.save(new Role(null, RoleName.STUDENT)));

            if (!studentRepository.existsByUsernameIgnoreCase("admin")) {
                Student admin = new Student();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setEmail("admin@gmail.com");
                admin.getRoles().add(adminRole);
                studentRepository.save(admin);
            }

            if (!studentRepository.existsByUsernameIgnoreCase("student")) {
                Student student = new Student();
                student.setUsername("student");
                student.setPassword(passwordEncoder.encode("123456"));
                student.setEmail("student@gmail.com");
                student.getRoles().add(studentRole);
                studentRepository.save(student);
            }

            Category it = categoryRepository.findByNameIgnoreCase("Công nghệ thông tin")
                    .orElseGet(() -> categoryRepository.save(new Category(null, "Công nghệ thông tin")));

            Category business = categoryRepository.findByNameIgnoreCase("Kinh tế")
                    .orElseGet(() -> categoryRepository.save(new Category(null, "Kinh tế")));

            Category english = categoryRepository.findByNameIgnoreCase("Ngoại ngữ")
                    .orElseGet(() -> categoryRepository.save(new Category(null, "Ngoại ngữ")));

            if (courseRepository.count() == 0) {
                courseRepository.save(new Course(null, "Lập trình Java Spring Boot",
                        "https://picsum.photos/seed/springboot/600/400",
                        3, "Nguyễn Văn A", it));

                courseRepository.save(new Course(null, "Cấu trúc dữ liệu và giải thuật",
                        "https://picsum.photos/seed/dsa/600/400",
                        4, "Trần Thị B", it));

                courseRepository.save(new Course(null, "Cơ sở dữ liệu",
                        "https://picsum.photos/seed/database/600/400",
                        3, "Lê Văn C", it));

                courseRepository.save(new Course(null, "Lập trình Web",
                        "https://picsum.photos/seed/web/600/400",
                        3, "Phạm Thị D", it));

                courseRepository.save(new Course(null, "Phân tích thiết kế hệ thống",
                        "https://picsum.photos/seed/system/600/400",
                        3, "Hoàng Văn E", it));

                courseRepository.save(new Course(null, "Kinh tế vi mô",
                        "https://picsum.photos/seed/economy/600/400",
                        2, "Đỗ Thị F", business));

                courseRepository.save(new Course(null, "Marketing căn bản",
                        "https://picsum.photos/seed/marketing/600/400",
                        2, "Bùi Văn G", business));

                courseRepository.save(new Course(null, "Tiếng Anh giao tiếp",
                        "https://picsum.photos/seed/english/600/400",
                        2, "Võ Thị H", english));
            }
        };
    }
}