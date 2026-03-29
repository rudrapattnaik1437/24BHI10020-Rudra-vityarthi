package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Course class demonstrating Builder pattern and encapsulation
 */
public class Course {
    private CourseCode code;
    private String title;
    private int credits;
    private String instructor;
    private Semester semester;
    private String department;
    private LocalDate createdDate;
    private boolean active;
    
    // Private constructor for Builder pattern
    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.semester = builder.semester;
        this.department = builder.department;
        this.createdDate = LocalDate.now();
        this.active = true;
    }
    
    // Builder pattern implementation
    public static class Builder {
        private CourseCode code;
        private String title;
        private int credits;
        private String instructor;
        private Semester semester;
        private String department;
        
        public Builder(CourseCode code, String title) {
            this.code = Objects.requireNonNull(code, "Course code cannot be null");
            this.title = Objects.requireNonNull(title, "Course title cannot be null");
        }
        
        public Builder credits(int credits) {
            if (credits <= 0) throw new IllegalArgumentException("Credits must be positive");
            this.credits = credits;
            return this;
        }
        
        public Builder instructor(String instructor) {
            this.instructor = Objects.requireNonNull(instructor, "Instructor cannot be null");
            return this;
        }
        
        public Builder semester(Semester semester) {
            this.semester = Objects.requireNonNull(semester, "Semester cannot be null");
            return this;
        }
        
        public Builder department(String department) {
            this.department = Objects.requireNonNull(department, "Department cannot be null");
            return this;
        }
        
        public Course build() {
            Objects.requireNonNull(instructor, "Instructor is required");
            Objects.requireNonNull(semester, "Semester is required");
            Objects.requireNonNull(department, "Department is required");
            if (credits <= 0) throw new IllegalStateException("Credits must be set and positive");
            
            return new Course(this);
        }
    }
    
    // Encapsulation - getters and setters
    public CourseCode getCode() { return code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = Objects.requireNonNull(title, "Title cannot be null"); 
    }
    
    public int getCredits() { return credits; }
    public void setCredits(int credits) {
        if (credits <= 0) throw new IllegalArgumentException("Credits must be positive");
        this.credits = credits;
    }
    
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { 
        this.instructor = Objects.requireNonNull(instructor, "Instructor cannot be null"); 
    }
    
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { 
        this.semester = Objects.requireNonNull(semester, "Semester cannot be null"); 
    }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { 
        this.department = Objects.requireNonNull(department, "Department cannot be null"); 
    }
    
    public LocalDate getCreatedDate() { return createdDate; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        return String.format("Course{code=%s, title='%s', credits=%d, instructor='%s', semester=%s, department='%s'}", 
                code, title, credits, instructor, semester, department);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return Objects.equals(code, course.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}