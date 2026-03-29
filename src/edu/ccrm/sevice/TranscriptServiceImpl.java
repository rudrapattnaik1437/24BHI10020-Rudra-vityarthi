package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of TranscriptService demonstrating streams and aggregation
 */
public class TranscriptServiceImpl implements TranscriptService {
    private final StudentService studentService;
    private final CourseService courseService;
    
    // Static nested class for transcript formatting
    public static class TranscriptBuilder {
        private StringBuilder transcript;
        private Student student;
        private double gpa;
        
        public TranscriptBuilder(Student student) {
            this.student = student;
            this.transcript = new StringBuilder();
            this.gpa = student.calculateGPA();
        }
        
        public TranscriptBuilder addHeader() {
            transcript.append("=".repeat(60)).append("\n");
            transcript.append("OFFICIAL TRANSCRIPT\n");
            transcript.append("=".repeat(60)).append("\n");
            transcript.append(String.format("Student: %s\n", student.getName().getFullName()));
            transcript.append(String.format("ID: %s\n", student.getId()));
            transcript.append(String.format("Registration No: %s\n", student.getRegNo()));
            transcript.append(String.format("Status: %s\n", student.getStatus()));
            transcript.append("-".repeat(60)).append("\n");
            return this;
        }
        
        public TranscriptBuilder addCourses() {
            transcript.append("COURSES AND GRADES:\n");
            transcript.append(String.format("%-10s %-30s %-8s %-5s %-5s\n", 
                    "Code", "Title", "Credits", "Grade", "Points"));
            transcript.append("-".repeat(60)).append("\n");
            
            student.getGrades().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey((c1, c2) -> c1.getCode().getCode().compareTo(c2.getCode().getCode())))
                    .forEach(entry -> {
                        Course course = entry.getKey();
                        Grade grade = entry.getValue();
                        transcript.append(String.format("%-10s %-30s %-8d %-5s %-5.1f\n",
                                course.getCode().getCode(),
                                course.getTitle().length() > 30 ? course.getTitle().substring(0, 27) + "..." : course.getTitle(),
                                course.getCredits(),
                                grade.name(),
                                grade.getGradePoints()));
                    });
            
            return this;
        }
        
        public TranscriptBuilder addSummary() {
            transcript.append("-".repeat(60)).append("\n");
            transcript.append(String.format("Total Credits: %d\n", student.getTotalCredits()));
            transcript.append(String.format("GPA: %.2f\n", gpa));
            transcript.append("=".repeat(60)).append("\n");
            return this;
        }
        
        public String build() {
            return transcript.toString();
        }
    }
    
    public TranscriptServiceImpl(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }
    
    @Override
    public void recordGrade(String studentId, String courseCode, Grade grade) {
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        
        Course course = courseService.findById(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseCode));
        
        student.assignGrade(course, grade);
    }
    
    @Override
    public double calculateGPA(String studentId) {
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        
        return student.calculateGPA();
    }
    
    @Override
    public String generateTranscript(String studentId) {
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        
        // Builder pattern demonstration
        return new TranscriptBuilder(student)
                .addHeader()
                .addCourses()
                .addSummary()
                .build();
    }
    
    // Stream operations with aggregation
    @Override
    public Map<String, Double> getGPADistribution() {
        return studentService.getAllStudents().stream()
                .collect(Collectors.groupingBy(
                        student -> {
                            double gpa = student.calculateGPA();
                            if (gpa >= 9.0) return "Excellent (9.0+)";
                            else if (gpa >= 8.0) return "Very Good (8.0-8.9)";
                            else if (gpa >= 7.0) return "Good (7.0-7.9)";
                            else if (gpa >= 6.0) return "Average (6.0-6.9)";
                            else return "Below Average (<6.0)";
                        },
                        Collectors.averagingDouble(Student::calculateGPA)
                ));
    }
    
    @Override
    public List<Student> getTopStudents(int count) {
        return studentService.getAllStudents().stream()
                .filter(student -> !student.getGrades().isEmpty())
                .sorted((s1, s2) -> Double.compare(s2.calculateGPA(), s1.calculateGPA()))
                .limit(count)
                .collect(Collectors.toList());
    }
}