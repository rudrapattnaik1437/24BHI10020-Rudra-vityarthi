package edu.ccrm.domain;

import java.util.Objects;

/**
 * Instructor class extending Person - demonstrates inheritance
 */
public class Instructor extends Person {
    private String employeeId;
    private String department;
    private String specialization;
    private double salary;
    
    public Instructor(String id, Name name, String email, String employeeId, String department) {
        super(id, name, email);
        this.employeeId = Objects.requireNonNull(employeeId, "Employee ID cannot be null");
        this.department = Objects.requireNonNull(department, "Department cannot be null");
    }
    
    // Method overriding - polymorphism
    @Override
    public String getRole() {
        return "Instructor";
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("Instructor: %s (%s) - %s Department", 
                getName().getFullName(), employeeId, department);
    }
    
    // Encapsulation
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { 
        this.employeeId = Objects.requireNonNull(employeeId, "Employee ID cannot be null"); 
    }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { 
        this.department = Objects.requireNonNull(department, "Department cannot be null"); 
    }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) {
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.salary = salary;
    }
    
    @Override
    public String toString() {
        return String.format("Instructor{id='%s', name=%s, employeeId='%s', department='%s', specialization='%s'}", 
                getId(), getName(), employeeId, department, specialization);
    }
}