package edu.ccrm.domain;

/**
 * Custom unchecked exception for credit limit exceeded
 * Demonstrates custom runtime exception
 */
public class MaxCreditLimitExceededException extends RuntimeException {
    private final int currentCredits;
    private final int maxCredits;
    private final int attemptedCredits;
    
    public MaxCreditLimitExceededException(int currentCredits, int maxCredits, int attemptedCredits) {
        super(String.format("Credit limit exceeded. Current: %d, Max: %d, Attempted to add: %d", 
                currentCredits, maxCredits, attemptedCredits));
        this.currentCredits = currentCredits;
        this.maxCredits = maxCredits;
        this.attemptedCredits = attemptedCredits;
    }
    
    public MaxCreditLimitExceededException(String message, int currentCredits, int maxCredits, int attemptedCredits) {
        super(message);
        this.currentCredits = currentCredits;
        this.maxCredits = maxCredits;
        this.attemptedCredits = attemptedCredits;
    }
    
    public int getCurrentCredits() { return currentCredits; }
    public int getMaxCredits() { return maxCredits; }
    public int getAttemptedCredits() { return attemptedCredits; }
}