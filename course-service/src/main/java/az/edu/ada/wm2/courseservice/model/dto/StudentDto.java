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
public class StudentDto {

    @Schema(description = "Tələbənin id-si", example = "15")
    private Long id;

    @Schema(description = "Tələbənin adı", example = "Nicat")
    private String firstName;

    @Schema(description = "Tələbənin soyadı", example = "Aliyev")
    private String lastName;

    @Schema(description = "Tələbənin elektron poçtu", example = "nicat.aliyev@example.com")
    private String email;

    @Schema(description = "Tələbənin yaşı", example = "20")
    private Integer age;
}