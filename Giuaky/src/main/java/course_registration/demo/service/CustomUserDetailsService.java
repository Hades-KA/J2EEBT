package course_registration.demo.service;

import course_registration.demo.entity.Student;
import course_registration.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByUsernameIgnoreCase(username)
                .or(() -> studentRepository.findByEmailIgnoreCase(username))
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));

        return User.builder()
                .username(student.getUsername())
                .password(student.getPassword())
                .authorities(
                        student.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                                .collect(Collectors.toSet())
                )
                .build();
    }
}