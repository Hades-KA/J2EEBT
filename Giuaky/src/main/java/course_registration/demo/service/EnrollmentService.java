package course_registration.demo.service;

import course_registration.demo.entity.Course;
import course_registration.demo.entity.Enrollment;
import course_registration.demo.entity.Student;
import course_registration.demo.repository.CourseRepository;
import course_registration.demo.repository.EnrollmentRepository;
import course_registration.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    private Student findCurrentStudent(String loginName) {
        return studentRepository.findByUsernameIgnoreCase(loginName)
                .or(() -> studentRepository.findByEmailIgnoreCase(loginName))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
    }

    public void enrollCourse(String loginName, Long courseId) {
        Student student = findCurrentStudent(loginName);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));

        if (enrollmentRepository.existsByStudent_IdAndCourse_Id(student.getId(), courseId)) {
            throw new RuntimeException("Bạn đã đăng ký học phần này rồi");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDateTime.now());

        enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getMyCourses(String loginName) {
        Student student = findCurrentStudent(loginName);
        return enrollmentRepository.findByStudent_IdOrderByEnrollDateDesc(student.getId());
    }

    @Transactional
    public void deleteEnrollmentsByCourseId(Long courseId) {
        enrollmentRepository.deleteByCourseId(courseId);
    }
}