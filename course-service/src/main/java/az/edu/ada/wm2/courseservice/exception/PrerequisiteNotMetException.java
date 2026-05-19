package az.edu.ada.wm2.courseservice.exception;

public class PrerequisiteNotMetException extends RuntimeException {

    public PrerequisiteNotMetException(Long studentId, Long courseId, Long prerequisiteCourseId) {
        super(String.format(
                "Student %d cannot enroll in course %d because the prerequisite course %d has not been completed.",
                studentId, courseId, prerequisiteCourseId
        ));
    }
}