package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of StudentService demonstrating polymorphism
 * Uses collections and stream operations
 */
public class StudentServiceImpl implements StudentService {
    private final Map<String, Student> students;
    private final Map<String, Student> studentsByRegNo;
    
    public StudentServiceImpl() {
        this.students = new HashMap<>();
        this.studentsByRegNo = new HashMap<>();
    }
    
    @Override
    public void addStudent(Student student) {
        Objects.requireNonNull(student, "Student cannot be null");
        
        if (students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " already exists");
        }
        
        if (studentsByRegNo.containsKey(student.getRegNo())) {
            throw new IllegalArgumentException("Student with registration number " + student.getRegNo() + " already exists");
        }
        
        students.put(student.getId(), student);
        studentsByRegNo.put(student.getRegNo(), student);
    }
    
    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    @Override
    public void updateStudent(Student student) {
        Objects.requireNonNull(student, "Student cannot be null");
        
        if (!students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " not found");
        }
        
        Student existing = students.get(student.getId());
        studentsByRegNo.remove(existing.getRegNo());
        
        students.put(student.getId(), student);
        studentsByRegNo.put(student.getRegNo(), student);
    }
    
    @Override
    public void deactivateStudent(String studentId) {
        Student student = students.get(studentId);
        if (student != null) {
            student.setActive(false);
            student.setStatus(Student.StudentStatus.INACTIVE);
        }
    }
    
    @Override
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(students.get(id));
    }
    
    @Override
    public Optional<Student> findByRegNo(String regNo) {
        return Optional.ofNullable(studentsByRegNo.get(regNo));
    }
    
    // Stream API demonstration with lambda expressions
    @Override
    public List<Student> search(String query) {
        return students.values().stream()
                .filter(student -> student.getName().getFullName().toLowerCase().contains(query.toLowerCase()) ||
                                 student.getRegNo().toLowerCase().contains(query.toLowerCase()) ||
                                 student.getId().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public void enrollStudentInCourse(String studentId, Course course) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        
        student.enrollInCourse(course);
    }
    
    @Override
    public void unenrollStudentFromCourse(String studentId, Course course) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        
        student.unenrollFromCourse(course);
    }
}