package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Enrollment class representing student-course relationship
 * Demonstrates composition and business logic
 */
public class Enrollment {
    private final Student student;
    private final Course course;
    private final LocalDate enrollmentDate;
    private Grade grade;
    private EnrollmentStatus status;
    
    public enum EnrollmentStatus {
        ENROLLED, DROPPED, COMPLETED
    }
    
    public Enrollment(Student student, Course course) {
        this.student = Objects.requireNonNull(student, "Student cannot be null");
        this.course = Objects.requireNonNull(course, "Course cannot be null");
        this.enrollmentDate = LocalDate.now();
        this.status = EnrollmentStatus.ENROLLED;
    }
    
    // Encapsulation
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    
    public Grade getGrade() { return grade; }
    public void setGrade(Grade grade) { this.grade = grade; }
    
    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { 
        this.status = Objects.requireNonNull(status, "Status cannot be null"); 
    }
    
    public boolean hasGrade() { return grade != null; }
    
    @Override
    public String toString() {
        return String.format("Enrollment{student=%s, course=%s, date=%s, grade=%s, status=%s}", 
                student.getName(), course.getCode(), enrollmentDate, 
                grade != null ? grade : "Not Graded", status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Enrollment that = (Enrollment) obj;
        return Objects.equals(student, that.student) && 
               Objects.equals(course, that.course);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }
}