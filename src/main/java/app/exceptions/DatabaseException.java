package app.exceptions;
import java.sql.SQLException;

public class DatabaseException extends Throwable {
    public DatabaseException(SQLException ex, String s) {
        System.out.println(ex.getMessage());
    }
}

