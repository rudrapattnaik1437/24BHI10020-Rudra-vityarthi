package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.List;
import java.util.Map;

/**
 * Service interface for transcript operations
 */
public interface TranscriptService {
    void recordGrade(String studentId, String courseCode, Grade grade);
    double calculateGPA(String studentId);
    String generateTranscript(String studentId);
    Map<String, Double> getGPADistribution();
    List<Student> getTopStudents(int count);
}