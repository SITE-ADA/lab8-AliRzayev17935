package az.edu.ada.wm2.courseservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {

    @Schema(description = "Kursun adı", example = "Verilənlər Strukturu")
    @NotBlank(message = "Ad mütləqdir")
    private String title;

    @Schema(description = "Kurs kodu", example = "CS201")
    @NotBlank(message = "Kod mütləqdir")
    private String code;

    @Schema(description = "Kreditlərin sayı", example = "4")
    @Positive(message = "Kreditlər müsbət olmalıdır")
    private Integer credits;

    @Schema(
            description = "İstəyə bağlı ön şərt kursun id-si. Əgər kursun ön şərti yoxdursa, null olmalıdır.",
            example = "1",
            nullable = true
    )
    private Long prerequisiteCourseId;
}