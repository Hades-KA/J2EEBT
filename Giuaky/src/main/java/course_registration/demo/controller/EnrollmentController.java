package course_registration.demo.controller;

import course_registration.demo.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll/{courseId}")
    public String enrollCourse(@PathVariable Long courseId,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.enrollCourse(authentication.getName(), courseId);
            redirectAttributes.addFlashAttribute("success", "Đăng ký học phần thành công");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/my-courses")
    public String myCourses(Authentication authentication, Model model) {
        model.addAttribute("enrollments", enrollmentService.getMyCourses(authentication.getName()));
        return "my-courses";
    }
}