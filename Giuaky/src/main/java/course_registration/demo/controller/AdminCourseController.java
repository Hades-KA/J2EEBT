package course_registration.demo.controller;

import course_registration.demo.entity.Category;
import course_registration.demo.entity.Course;
import course_registration.demo.repository.CategoryRepository;
import course_registration.demo.repository.CourseRepository;
import course_registration.demo.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.*;
import java.util.UUID;

@Controller
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final EnrollmentService enrollmentService;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        return "admin/course-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
        return "admin/course-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));

        model.addAttribute("course", course);
        model.addAttribute("categories", categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
        return "admin/course-form";
    }

    @PostMapping("/save")
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
                             BindingResult result,
                             @RequestParam(value = "categoryId", required = false) Long categoryId,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
            return "admin/course-form";
        }

        // Category
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            course.setCategory(category);
        } else {
            course.setCategory(null);
        }

        // Upload ảnh nếu có
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Xóa ảnh cũ (nếu ảnh cũ là file upload)
                deleteOldImageIfExists(course.getId());

                String original = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String ext = StringUtils.getFilenameExtension(original);
                String filename = UUID.randomUUID() + (ext != null ? "." + ext : "");

                Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
                Files.createDirectories(dir);

                Path target = dir.resolve(filename);
                Files.copy(imageFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

                course.setImage("/uploads/" + filename);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Tải ảnh lên thất bại: " + e.getMessage());
                return "redirect:/admin/courses";
            }
        }

        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("success", "Lưu học phần thành công");
        return "redirect:/admin/courses";
    }

    private void deleteOldImageIfExists(Long courseId) {
        if (courseId == null) return;

        Course existing = courseRepository.findById(courseId).orElse(null);
        if (existing == null) return;

        String img = existing.getImage();
        if (img == null) return;

        // Chỉ xóa nếu là ảnh upload nội bộ (/uploads/...)
        if (img.startsWith("/uploads/")) {
            try {
                String filename = img.substring("/uploads/".length());
                Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(filename);
                Files.deleteIfExists(filePath);
            } catch (Exception ignored) {
            }
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // xóa enrollment trước
            enrollmentService.deleteEnrollmentsByCourseId(id);

            // xóa ảnh nếu là file upload
            deleteOldImageIfExists(id);

            courseRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa học phần thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa học phần này");
        }
        return "redirect:/admin/courses";
    }
}