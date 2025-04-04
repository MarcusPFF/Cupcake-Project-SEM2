package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerMapperTest {

    private static ConnectionPool connectionPool;
    private static CustomerMapper customerMapper;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        String USER = "postgres";
        String PASSWORD = "postgres";
        String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=test";
        String DB = "Cupcake";
        connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

        setupTestDatabase();
    }

    @BeforeEach
    public void clearAndSetupDatabase() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            connection.createStatement().execute("""
            DROP TABLE IF EXISTS cupcake_customers CASCADE;
        """);
            customerMapper = new CustomerMapper();
            setupTestDatabase();
            insertTestData();
        }
    }

    private static void setupTestDatabase() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            String createTablesSQL = """
            CREATE TABLE IF NOT EXISTS cupcake_customers (
                customer_id SERIAL PRIMARY KEY,
                customer_email VARCHAR(255) NOT NULL,
                customer_name VARCHAR(255) NOT NULL,
                customer_password VARCHAR(255) NOT NULL,
                customer_wallet FLOAT NOT NULL
            );
        """;
            connection.createStatement().execute(createTablesSQL);
        }
    }

    public void insertTestData() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            String insertSQL = """
            INSERT INTO cupcake_customers (customer_email, customer_name, customer_password, customer_wallet)
            VALUES ('test@example.com', 'John Doe', 'password123', 100.0),
                   ('alice@example.com', 'Alice Smith', 'password321', 50.0),
                   ('bob@example.com', 'Bob Johnson', 'password456', 200.0);
        """;
            connection.createStatement().execute(insertSQL);
        }
    }


    @Test
    public void testGetEmailFromCustomerName() throws DatabaseException, SQLException {
        String email = "test@example.com";
        String name = "John Doe";

        String retrievedEmail = customerMapper.getEmailFromCustomerName(connectionPool, name);

        assertEquals(email, retrievedEmail);
    }

    @Test
    public void testGetCustomerNameFromEmail() throws DatabaseException, SQLException {
        String email = "test@example.com";
        String name = "John Doe";

        String retrievedName = customerMapper.getCustomerNameFromEmail(connectionPool, email);

        assertEquals(name, retrievedName);
    }

    @Test
    public void testVerifyUserCredentials() throws DatabaseException, SQLException {
        String name = "John Doe";
        String password = "password123";

        boolean isVerified = customerMapper.verifyUserCredentials(connectionPool, name, password);
        assertTrue(isVerified);

        boolean isNotVerified = customerMapper.verifyUserCredentials(connectionPool, name, "wrongpassword");
        assertFalse(isNotVerified);
    }

    @Test
    public void testGetCustomerIdFromEmail() throws DatabaseException, SQLException {
        String email = "test@example.com";

        int customerId = customerMapper.getCustomerIdFromEmail(connectionPool, email);

        assertTrue(customerId > 0);
    }

    @Test
    public void testGetAllCustomers() throws DatabaseException, SQLException {
        String name = "John Doe";


        List<Customer> customers = customerMapper.getAllCustomers(connectionPool);

        assertNotNull(customers);
        assertTrue(customers.size() > 0);

        boolean customerFound = customers.stream().anyMatch(c -> c.getName().equals(name));
        assertTrue(customerFound);
    }

    @Test
    public void testRemoveCustomerById() throws DatabaseException, SQLException {
        String email = "test@example.com";

        int customerId = customerMapper.getCustomerIdFromEmail(connectionPool, email);

        customerMapper.removeCustomerById(connectionPool, customerId);

        assertThrows(DatabaseException.class, () -> customerMapper.getCustomerIdFromEmail(connectionPool, email));
    }

    @Test
    public void testGetWalletFromEmail() throws DatabaseException, SQLException {
        String email = "test@example.com";
        float wallet = 100.0f;

        float retrievedWallet = customerMapper.getWalletFromEmail(connectionPool, email);

        assertEquals(wallet, retrievedWallet);
    }

    @Test
    public void testSetWallet() throws DatabaseException, SQLException {
        String email = "test@example.com";

        double newWalletAmount = 150.0;
        customerMapper.setWallet(connectionPool, newWalletAmount, email);

        float updatedWallet = customerMapper.getWalletFromEmail(connectionPool, email);
        assertEquals(newWalletAmount, updatedWallet, 0.001);
    }
}
