package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Student;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for student operations
 */
public interface StudentService extends Searchable<Student> {
    void addStudent(Student student);
    List<Student> getAllStudents();
    void updateStudent(Student student);
    void deactivateStudent(String studentId);
    Optional<Student> findByRegNo(String regNo);
    void enrollStudentInCourse(String studentId, Course course);
    void unenrollStudentFromCourse(String studentId, Course course);
}