package edu.ccrm.domain;

/**
 * Enum for grades with grade points
 * Demonstrates enum with constructors and fields
 */
public enum Grade {
    S(10.0, "Outstanding !"), 
    A(9.0, "Very Good !"), 
    B(8.0, "Good !"), 
    C(7.0, "Average"), 
    D(6.0, "Below Average"), 
    E(5.0, "Poor"), 
    F(0.0, "Fail");
    
    private final double gradePoints;
    private final String description;
    
    // Enum constructor
    Grade(double gradePoints, String description) {
        this.gradePoints = gradePoints;
        this.description = description;
    }
    
    public double getGradePoints() { return gradePoints; }
    public String getDescription() { return description; }
    
    public boolean isPassing() { return gradePoints >= 5.0; }
    
    @Override
    public String toString() {
        return String.format("%s (%.1f) - %s", name(), gradePoints, description);
    }
}