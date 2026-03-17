package course_registration.demo.controller;

import course_registration.demo.entity.Course;
import course_registration.demo.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CourseRepository courseRepository;

    @GetMapping({"/", "/home", "/courses"})
    public String home(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {

        if (page < 0) {
            page = 0;
        }

        keyword = keyword == null ? "" : keyword.trim();

        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<Course> coursePage;

        if (keyword.isEmpty()) {
            coursePage = courseRepository.findAll(pageable);
        } else {
            coursePage = courseRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }

        model.addAttribute("coursePage", coursePage);
        model.addAttribute("keyword", keyword);

        return "home";
    }
}