package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.config.AppConfig;
import java.io.IOException;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

/**
 * CSV Import/Export service using NIO.2 and Streams
 * Demonstrates file I/O operations
 */
public class ImportExportService {
    private final AppConfig config;
    private final DateTimeFormatter dateFormat;
    
    public ImportExportService() {
        this.config = AppConfig.getInstance();
        this.dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
    }
    
    // Create data directory if it doesn't exist
    private void ensureDataDirectory() throws IOException {
        Path dataPath = config.getDataPath();
        if (!Files.exists(dataPath)) {
            Files.createDirectories(dataPath);
        }
    }
    
    // Import students from CSV using Streams and NIO.2
    public List<Student> importStudents(String filename) throws IOException {
        ensureDataDirectory();
        Path filePath = config.getDataPath().resolve(filename);
        
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }
        
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines
                    .skip(1) // Skip header
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseStudentFromCSV)
                    .toList(); // Java 16+ toList()
        }
    }
    
    private Student parseStudentFromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid CSV format for student: " + csvLine);
        }
        
        String id = parts[0].trim();
        String regNo = parts[1].trim();
        String[] nameParts = parts[2].trim().split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        String email = parts[3].trim();
        
        Name name = new Name(firstName, lastName);
        return new Student(id, name, email, regNo);
    }
    
    // Import courses from CSV
    public List<Course> importCourses(String filename) throws IOException {
        ensureDataDirectory();
        Path filePath = config.getDataPath().resolve(filename);
        
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }
        
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines
                    .skip(1) // Skip header
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseCourseFromCSV)
                    .toList();
        }
    }
    
    private Course parseCourseFromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid CSV format for course: " + csvLine);
        }
        
        CourseCode code = new CourseCode(parts[0].trim());
        String title = parts[1].trim();
        int credits = Integer.parseInt(parts[2].trim());
        String instructor = parts[3].trim();
        Semester semester = Semester.valueOf(parts[4].trim().toUpperCase());
        String department = parts[5].trim();
        
        return new Course.Builder(code, title)
                .credits(credits)
                .instructor(instructor)
                .semester(semester)
                .department(department)
                .build();
    }
    
    // Export students to CSV
    public void exportStudents(List<Student> students, String filename) throws IOException {
        ensureDataDirectory();
        Path filePath = config.getDataPath().resolve(filename);
        
        StringBuilder csv = new StringBuilder();
        csv.append("ID,RegNo,Name,Email,Status,CreatedDate\n");
        
        students.forEach(student -> {
            csv.append(String.format("%s,%s,%s,%s,%s,%s\n",
                    student.getId(),
                    student.getRegNo(),
                    student.getName().getFullName(),
                    student.getEmail(),
                    student.getStatus(),
                    student.getCreatedDate().format(dateFormat)));
        });
        
        Files.writeString(filePath, csv.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    // Export courses to CSV
    public void exportCourses(List<Course> courses, String filename) throws IOException {
        ensureDataDirectory();
        Path filePath = config.getDataPath().resolve(filename);
        
        StringBuilder csv = new StringBuilder();
        csv.append("Code,Title,Credits,Instructor,Semester,Department,CreatedDate\n");
        
        courses.forEach(course -> {
            csv.append(String.format("%s,%s,%d,%s,%s,%s,%s\n",
                    course.getCode().getCode(),
                    course.getTitle(),
                    course.getCredits(),
                    course.getInstructor(),
                    course.getSemester(),
                    course.getDepartment(),
                    course.getCreatedDate().format(dateFormat)));
        });
        
        Files.writeString(filePath, csv.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    // Export enrollments with grades
    public void exportEnrollments(List<Student> students, String filename) throws IOException {
        ensureDataDirectory();
        Path filePath = config.getDataPath().resolve(filename);
        
        StringBuilder csv = new StringBuilder();
        csv.append("StudentID,StudentName,CourseCode,CourseTitle,Grade,GradePoints\n");
        
        students.forEach(student -> {
            student.getGrades().forEach((course, grade) -> {
                csv.append(String.format("%s,%s,%s,%s,%s,%.1f\n",
                        student.getId(),
                        student.getName().getFullName(),
                        course.getCode().getCode(),
                        course.getTitle(),
                        grade.name(),
                        grade.getGradePoints()));
            });
        });
        
        Files.writeString(filePath, csv.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}