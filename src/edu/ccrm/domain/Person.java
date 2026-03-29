package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Abstract base class demonstrating inheritance and abstraction
 * Encapsulates common properties for Student and Instructor
 */
public abstract class Person {
    private final String id;
    private Name name;
    private String email;
    private LocalDate createdDate;
    private boolean active;
    
    // Constructor demonstrating inheritance concepts
    protected Person(String id, Name name, String email) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.createdDate = LocalDate.now();
        this.active = true;
    }
    
    // Abstract methods demonstrating abstraction
    public abstract String getRole();
    public abstract String getDisplayInfo();
    
    // Encapsulation - getters and setters
    public String getId() { return id; }
    
    public Name getName() { return name; }
    public void setName(Name name) { 
        this.name = Objects.requireNonNull(name, "Name cannot be null"); 
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        this.email = Objects.requireNonNull(email, "Email cannot be null"); 
    }
    
    public LocalDate getCreatedDate() { return createdDate; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    // Method overriding
    @Override
    public String toString() {
        return String.format("%s{id='%s', name=%s, email='%s', active=%s}", 
                getClass().getSimpleName(), id, name, email, active);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(id, person.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}