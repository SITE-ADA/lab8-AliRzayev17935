package az.edu.ada.wm2.courseservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudentsResponseDto {

    @Schema(description = "Kursun id-si", example = "1")
    private Long courseId;

    @Schema(description = "Kursun adı", example = "Verilənlər Strukturu")
    private String courseTitle;

    @Schema(description = "Bu kursa qeydiyyatdan keçmiş tələbələr")
    private List<StudentDto> students;
}