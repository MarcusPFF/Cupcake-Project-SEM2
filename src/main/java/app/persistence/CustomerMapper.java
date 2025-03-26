package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CustomerMapper {

    //Til signup
    public void addNewCustomer(ConnectionPool connectionPool, String email, String name, String password, float wallet) throws DatabaseException {
        String sql = "INSERT INTO cupcake_customers (customer_email, customer_name, customer_password, customer_wallet) " + "VALUES (?, ?, ?,0) " + "ON CONFLICT (customer_email) DO NOTHING;";

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



    //TODO tjek om denne metode virker
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

    public int getToppingIdFromToppingFlavour(ConnectionPool connectionPool, String toppingFlavour) throws DatabaseException {
        String sql = "SELECT topping_id FROM cupcake_toppings WHERE topping_flavour = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, toppingFlavour);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (rs.getInt("topping_id"));

                } else {
                    throw new DatabaseException(null, "Could not get topping id from database");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not get topping from database");
        }
    }

    public int getBottomIdFromBottomFlavour(ConnectionPool connectionPool, String bottomFlavour) throws DatabaseException {
        String sql = "SELECT bottom_id FROM cupcake_toppings WHERE bottom_flavour = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, bottomFlavour);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (rs.getInt("bottom_id"));

                } else {
                    throw new DatabaseException(null, "Could not get bottom id from database");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not get bottom from database");
        }
    }
}
