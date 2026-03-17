package course_registration.demo.controller;

import course_registration.demo.dto.RegisterRequest;
import course_registration.demo.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final StudentService studentService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "register";
        }

        try {
            studentService.registerStudent(registerRequest);
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công. Hãy đăng nhập.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            result.reject("registerError", e.getMessage());
            return "register";
        }
    }
}