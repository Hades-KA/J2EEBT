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
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String rawEmail = oidcUser.getAttribute("email");
        if (rawEmail == null || rawEmail.isBlank()) {
            throw new OAuth2AuthenticationException("Google không trả về email. Hãy kiểm tra scope openid,profile,email.");
        }

        // Tạo biến mới, KHÔNG gán lại biến dùng trong lambda
        final String email = rawEmail.trim().toLowerCase();

        Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                .orElseThrow(() -> new RuntimeException("Role STUDENT chưa tồn tại"));

        Student student = studentRepository.findByEmailIgnoreCase(email).orElseGet(() -> {
            Student s = new Student();
            s.setUsername(email);
            s.setEmail(email);
            s.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            s.getRoles().add(studentRole);
            return studentRepository.save(s);
        });

        // Nếu thiếu role thì bổ sung
        if (student.getRoles() == null || student.getRoles().isEmpty()) {
            student.getRoles().add(studentRole);
            studentRepository.save(student);
        }

        Set<GrantedAuthority> authorities = student.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name()))
                .collect(Collectors.toSet());

        // "email" làm nameAttributeKey => sec:authentication="name" sẽ là email
        return new DefaultOidcUser(authorities, userRequest.getIdToken(), oidcUser.getUserInfo(), "email");
    }
}