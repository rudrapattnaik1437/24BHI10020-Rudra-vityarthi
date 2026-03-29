package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of CourseService demonstrating polymorphism
 * Uses streams and functional programming concepts
 */
public class CourseServiceImpl implements CourseService {
    private final Map<String, Course> courses;
    
    public CourseServiceImpl() {
        this.courses = new HashMap<>();
    }
    
    @Override
    public void addCourse(Course course) {
        Objects.requireNonNull(course, "Course cannot be null");
        
        String courseCode = course.getCode().getCode();
        if (courses.containsKey(courseCode)) {
            throw new IllegalArgumentException("Course with code " + courseCode + " already exists");
        }
        
        courses.put(courseCode, course);
    }
    
    @Override
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    @Override
    public void updateCourse(Course course) {
        Objects.requireNonNull(course, "Course cannot be null");
        
        String courseCode = course.getCode().getCode();
        if (!courses.containsKey(courseCode)) {
            throw new IllegalArgumentException("Course with code " + courseCode + " not found");
        }
        
        courses.put(courseCode, course);
    }
    
    @Override
    public void deactivateCourse(String courseCode) {
        Course course = courses.get(courseCode);
        if (course != null) {
            course.setActive(false);
        }
    }
    
    @Override
    public Optional<Course> findById(String courseCode) {
        return Optional.ofNullable(courses.get(courseCode));
    }
    
    // Stream API with method references and lambda expressions
    @Override
    public List<Course> search(String query) {
        return courses.values().stream()
                .filter(course -> course.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                course.getCode().getCode().toLowerCase().contains(query.toLowerCase()) ||
                                course.getInstructor().toLowerCase().contains(query.toLowerCase()) ||
                                course.getDepartment().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> findByInstructor(String instructor) {
        return courses.values().stream()
                .filter(course -> course.getInstructor().equalsIgnoreCase(instructor))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> findByDepartment(String department) {
        return courses.values().stream()
                .filter(course -> course.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> findBySemester(Semester semester) {
        return courses.values().stream()
                .filter(course -> course.getSemester() == semester)
                .collect(Collectors.toList());
    }
}