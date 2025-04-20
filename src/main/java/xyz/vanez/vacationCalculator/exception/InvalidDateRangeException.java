package xyz.vanez.vacationCalculator.exception;

public class InvalidDateRangeException extends RuntimeException{
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
