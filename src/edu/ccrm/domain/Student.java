package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Student class extending Person - demonstrates inheritance
 * Shows polymorphism and encapsulation
 */
public class Student extends Person {
    private String regNo;
    private final Set<Course> enrolledCourses;
    private final Map<Course, Grade> grades;
    private StudentStatus status;
    
    // Nested enum demonstrating nested types
    public enum StudentStatus {
        ACTIVE, INACTIVE, GRADUATED, SUSPENDED
    }
    
    public Student(String id, Name name, String email, String regNo) {
        super(id, name, email); // Constructor inheritance
        this.regNo = Objects.requireNonNull(regNo, "Registration number cannot be null");
        this.enrolledCourses = new HashSet<>();
        this.grades = new HashMap<>();
        this.status = StudentStatus.ACTIVE;
    }
    
    // Method overriding - polymorphism
    @Override
    public String getRole() {
        return "Student";
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("Student: %s (Reg: %s) - %d courses enrolled", 
                getName().getFullName(), regNo, enrolledCourses.size());
    }
    
    // Encapsulation
    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { 
        this.regNo = Objects.requireNonNull(regNo, "Registration number cannot be null"); 
    }
    
    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }
    
    // Defensive copying for collection access
    public Set<Course> getEnrolledCourses() {
        return new HashSet<>(enrolledCourses);
    }
    
    public Map<Course, Grade> getGrades() {
        return new HashMap<>(grades);
    }
    
    // Business logic methods
    public void enrollInCourse(Course course) {
        Objects.requireNonNull(course, "Course cannot be null");
        if (enrolledCourses.contains(course)) {
            throw new IllegalArgumentException("Student already enrolled in course: " + course.getCode());
        }
        enrolledCourses.add(course);
    }
    
    public void unenrollFromCourse(Course course) {
        Objects.requireNonNull(course, "Course cannot be null");
        if (!enrolledCourses.contains(course)) {
            throw new IllegalArgumentException("Student not enrolled in course: " + course.getCode());
        }
        enrolledCourses.remove(course);
        grades.remove(course);
    }
    
    public void assignGrade(Course course, Grade grade) {
        Objects.requireNonNull(course, "Course cannot be null");
        Objects.requireNonNull(grade, "Grade cannot be null");
        if (!enrolledCourses.contains(course)) {
            throw new IllegalArgumentException("Student not enrolled in course: " + course.getCode());
        }
        grades.put(course, grade);
    }
    
    // Calculate GPA using streams
    public double calculateGPA() {
        if (grades.isEmpty()) return 0.0;
        
        double totalPoints = grades.values().stream()
                .mapToDouble(Grade::getGradePoints)
                .sum();
        
        double totalCredits = grades.keySet().stream()
                .mapToInt(Course::getCredits)
                .sum();
        
        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }
    
    public int getTotalCredits() {
        return enrolledCourses.stream()
                .mapToInt(Course::getCredits)
                .sum();
    }
    
    // Method overriding
    @Override
    public String toString() {
        return String.format("Student{id='%s', name=%s, regNo='%s', status=%s, courses=%d, gpa=%.2f}", 
                getId(), getName(), regNo, status, enrolledCourses.size(), calculateGPA());
    }
}