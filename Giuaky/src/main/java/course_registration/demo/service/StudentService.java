package course_registration.demo.service;

import course_registration.demo.dto.RegisterRequest;
import course_registration.demo.entity.Role;
import course_registration.demo.entity.RoleName;
import course_registration.demo.entity.Student;
import course_registration.demo.repository.RoleRepository;
import course_registration.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerStudent(RegisterRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (studentRepository.existsByUsernameIgnoreCase(username)) {
            throw new RuntimeException("Username đã tồn tại");
        }

        if (studentRepository.existsByEmailIgnoreCase(email)) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                .orElseThrow(() -> new RuntimeException("Role STUDENT chưa tồn tại"));

        Student student = new Student();
        student.setUsername(username);
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setEmail(email);
        student.getRoles().add(studentRole);

        studentRepository.save(student);
    }
}