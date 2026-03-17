package course_registration.demo.config;

import course_registration.demo.entity.Role;
import course_registration.demo.entity.RoleName;
import course_registration.demo.entity.Student;
import course_registration.demo.repository.RoleRepository;
import course_registration.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String rawEmail = oauth2User.getAttribute("email");
        if (rawEmail == null || rawEmail.isBlank()) {
            throw new OAuth2AuthenticationException("Google không trả về email. Hãy kiểm tra scope openid,profile,email.");
        }
        String email = rawEmail.trim().toLowerCase();

        Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                .orElseThrow(() -> new RuntimeException("Role STUDENT chưa tồn tại"));

        // Tìm hoặc tạo student theo email
        Student student = studentRepository.findByEmailIgnoreCase(email).orElseGet(() -> {
            Student s = new Student();
            s.setUsername(email);
            s.setEmail(email);
            s.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            s.getRoles().add(studentRole);
            return studentRepository.save(s);
        });

        // Nếu student đã tồn tại nhưng thiếu role STUDENT thì tự bổ sung
        if (student.getRoles() == null || student.getRoles().isEmpty()) {
            student.getRoles().add(studentRole);
            studentRepository.save(student);
        }

        // Authorities đúng định dạng ROLE_STUDENT
        Set<GrantedAuthority> authorities = student.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name()))
                .collect(Collectors.toSet());

        // Bắt buộc: nameAttributeKey = "email" để Authentication.getName() là email
        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
        attributes.put("email", email);

        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}