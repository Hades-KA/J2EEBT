package course_registration.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên học phần không được để trống")
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String image;

    @NotNull(message = "Số tín chỉ không được để trống")
    @Min(value = 1, message = "Số tín chỉ phải >= 1")
    private Integer credits;

    @NotBlank(message = "Giảng viên không được để trống")
    @Column(nullable = false)
    private String lecturer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}