package az.edu.ada.wm2.studentservice.controller;

import az.edu.ada.wm2.studentservice.model.dto.StudentRequestDto;
import az.edu.ada.wm2.studentservice.model.dto.StudentResponseDto;
import az.edu.ada.wm2.studentservice.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Tələbələr", description = "Tələbələrin idarə olunması üçün son nöqtələr")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Tələbə yarat", description = "Yeni tələbə qeydiyyatı yaradır.")
    public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentRequestDto requestDto) {
        StudentResponseDto createdStudent = studentService.createStudent(requestDto);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Bütün tələbələri al", description = "Bütün tələbələrin siyahısını qaytarır.")
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/search")
    @Operation(
            summary = "Tələbələri ada görə axtar",
            description = "Adı və ya soyadı verilmiş mətni ehtiva edən tələbələri qaytarır (böyük-kiçik hərflərə həssas deyil)."
    )
    public ResponseEntity<List<StudentResponseDto>> searchStudentsByName(@RequestParam String name) {
        return ResponseEntity.ok(studentService.searchStudentsByName(name));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Tələbəni id-yə görə al", description = "Verilmiş id-yə uyğun tələbəni qaytarır.")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Tələbəni yenilə", description = "Mövcud tələbənin məlumatlarını id-yə görə yeniləyir.")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDto requestDto) {
        return ResponseEntity.ok(studentService.updateStudent(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Tələbəni sil", description = "Verilmiş id-yə uyğun tələbəni silir.")
    public ResponseEntity<StudentResponseDto> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}