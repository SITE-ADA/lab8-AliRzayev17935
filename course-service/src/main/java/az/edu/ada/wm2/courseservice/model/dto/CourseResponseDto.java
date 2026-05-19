package az.edu.ada.wm2.courseservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {

    @Schema(description = "Kursun id-si", example = "1")
    private Long id;

    @Schema(description = "Kursun adı", example = "Verilənlər Strukturu")
    private String title;

    @Schema(description = "Kurs kodu", example = "CS201")
    private String code;

    @Schema(description = "Kreditlərin sayı", example = "4")
    private Integer credits;

    @Schema(
            description = "Ön şərt kursun id-si. Bu kursun ön şərti yoxdursa null olur.",
            example = "1",
            nullable = true
    )
    private Long prerequisiteCourseId;
}