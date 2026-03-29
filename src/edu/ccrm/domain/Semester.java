package edu.ccrm.domain;

/**
 * Enum for academic semesters
 * Demonstrates enum usage
 */
public enum Semester {
    SPRING(1, "Spring Semester"),
    SUMMER(2, "Summer Semester"), 
    FALL(3, "Fall Semester"),
    WINTER(4, "Winter Semester");
    
    private final int order;
    private final String displayName;
    
    Semester(int order, String displayName) {
        this.order = order;
        this.displayName = displayName;
    }
    
    public int getOrder() { return order; }
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}