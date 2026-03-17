package course_registration.demo.repository;

import course_registration.demo.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    List<Enrollment> findByStudent_IdOrderByEnrollDateDesc(Long studentId);

    @Modifying
    @Query("delete from Enrollment e where e.course.id = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);
}