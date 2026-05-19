package az.edu.ada.wm2.studentservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {

    @Schema(description = "Tələbənin unikal identifikatoru", example = "1")
    private Long id;

    @Schema(description = "Tələbənin adı", example = "Nicat")
    private String firstName;

    @Schema(description = "Tələbənin soyadı", example = "Aliyev")
    private String lastName;

    @Schema(description = "Tələbənin elektron poçt ünvanı", example = "nicat.aliyev@example.com")
    private String email;

    @Schema(description = "Tələbənin yaşı", example = "20")
    private Integer age;
}