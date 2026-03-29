package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for course operations
 */
public interface CourseService extends Searchable<Course> {
    void addCourse(Course course);
    List<Course> getAllCourses();
    void updateCourse(Course course);
    void deactivateCourse(String courseCode);
    List<Course> findByInstructor(String instructor);
    List<Course> findByDepartment(String department);
    List<Course> findBySemester(Semester semester);
}