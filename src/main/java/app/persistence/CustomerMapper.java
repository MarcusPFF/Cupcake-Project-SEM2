package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CustomerMapper {
    private static Customer customer;

    //Til signup
    public void addNewCustomer(ConnectionPool connectionPool, String email, String name, String password, float wallet) throws DatabaseException {
        String sql = "INSERT INTO cupcake_customers (customer_email, customer_name, customer_password, customer_wallet) " + "VALUES (?, ?, ?,?) " + "ON CONFLICT (customer_email) DO NOTHING;";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, name);
                ps.setString(3, password);
                ps.setFloat(4, wallet);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("Customer already exists or insert failed.");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not get Customer from database");
        }
    }

    // Til login funktion
    public String getEmailFromCustomerName(ConnectionPool connectionPool, String customerName) throws DatabaseException {
        String sql = "SELECT customer_email FROM cupcake_customers WHERE customer_name = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, customerName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("customer_email");
                } else {
                    throw new DatabaseException(null, "Could not find email for the given customer name.");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not fetch email from database.");
        }
    }

    public String getCustomerNameFromEmail(ConnectionPool connectionPool, String customerEmail) throws DatabaseException {
        String sql = "SELECT customer_name FROM cupcake_customers WHERE customer_email = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, customerEmail);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("customer_name");
                } else {
                    throw new DatabaseException(null, "Could not find customer name for the given email.");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not fetch name from database.");
        }
    }

    public boolean verifyUserCredentials(ConnectionPool connectionPool, String customerName, String password) throws DatabaseException {
        String sql = "SELECT customer_id FROM cupcake_customers WHERE (customer_name = ? OR customer_email = ?) AND customer_password = ?;";
        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customerName);
            ps.setString(2, customerName);
            ps.setString(3, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not verify user credentials");
        }
    }

    public int getCustomerIdFromEmail(ConnectionPool connectionPool, String email) throws DatabaseException {
        String sql = "SELECT customer_id FROM cupcake_customers WHERE customer_email = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (rs.getInt("customer_id"));

                } else {
                    throw new DatabaseException(null, "Could not get customer id from database");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not get customer from database");
        }
    }

    public List<Customer> getAllCustomers(ConnectionPool connectionPool) throws DatabaseException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT customer_id, customer_email, customer_name, customer_password, customer_wallet FROM cupcake_customers;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String email = rs.getString("customer_email");
                String name = rs.getString("customer_name");
                String password = rs.getString("customer_password");
                float wallet = rs.getFloat("customer_wallet");
                customers.add(new Customer(id, email, name, password, wallet));
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not fetch customer list from database.");
        }
        return customers;
    }

    public void removeCustomerById(ConnectionPool connectionPool, int customerId) throws DatabaseException {
        String sql = "DELETE FROM cupcake_customers WHERE customer_id = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DatabaseException(null, "No customer found with ID: " + customerId);
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not delete customer from database");
        }
    }
}
