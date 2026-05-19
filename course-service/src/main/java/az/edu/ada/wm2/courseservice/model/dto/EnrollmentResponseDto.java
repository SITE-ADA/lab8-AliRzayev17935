package az.edu.ada.wm2.courseservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponseDto {

    @Schema(description = "Qeydiyyatın id-si", example = "10")
    private Long enrollmentId;

    @Schema(description = "Kursun id-si", example = "1")
    private Long courseId;

    @Schema(description = "Tələbənin id-si", example = "15")
    private Long studentId;

    @Schema(description = "Tələbənin kursa qeydiyyatdan keçdiyi tarix və vaxt", example = "2026-05-19T18:45:30")
    private LocalDateTime enrolledAt;

    @Schema(description = "Əməliyyatın nəticə mesajı", example = "Tələbə uğurla qeydiyyatdan keçirildi.")
    private String message;
}