package edu.ccrm.domain;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Immutable value class for course codes
 * Demonstrates validation and immutability
 */
public final class CourseCode {
    private static final Pattern COURSE_CODE_PATTERN = Pattern.compile("^[A-Z]{2,4}\\d{3,4}$");
    private final String code;
    
    public CourseCode(String code) {
        this.code = Objects.requireNonNull(code, "Course code cannot be null").toUpperCase().trim();
        
        if (!COURSE_CODE_PATTERN.matcher(this.code).matches()) {
            throw new IllegalArgumentException("Invalid course code format. Expected: 2-4 letters followed by 3-4 digits (e.g., CS101, MATH2001)");
        }
    }
    
    public String getCode() { return code; }
    
    @Override
    public String toString() { return code; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CourseCode that = (CourseCode) obj;
        return Objects.equals(code, that.code);
    }
    
    @Override
    public int hashCode() { return Objects.hash(code); }
}