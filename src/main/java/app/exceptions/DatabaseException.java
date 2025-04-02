package app.exceptions;

public class DatabaseException extends Exception {
    public DatabaseException(Throwable ex, String message) {
        super(message + (ex != null ? ": " + ex.getMessage() : ""));
    }
}