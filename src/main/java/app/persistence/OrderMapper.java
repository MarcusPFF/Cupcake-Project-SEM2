package app.persistence;

import app.entities.Cupcakes;
import app.entities.Order;
import app.exceptions.DatabaseException;
import com.fasterxml.jackson.databind.DatabindException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public class OrderMapper {


    public int addNewOrder(ConnectionPool connectionPool, int customerId) throws DatabaseException {
        // Modified SQL to return the generated order_id in one atomic operation
        String sql = "INSERT INTO customer_orders(customer_id, order_date, status_id) "
                + "VALUES (?, CURRENT_DATE, 1) "
                + "RETURNING order_id";  // <--- Critical fix here

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {  // Single statement

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {  // Use executeQuery() with RETURNING
                if (rs.next()) {
                    return rs.getInt("order_id");  // Directly return the generated ID
                } else {
                    System.out.println("!virker");
                    return 0;
                }
            }

        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not create order for customer: " + customerId);
        }
    }

    public void addNewOrderHistories(ConnectionPool connectionPool, int customerId, int orderId, ArrayList<Cupcakes> cart) throws DatabaseException {

        String sql = "INSERT INTO customer_order_history(order_id, bottom_id, topping_id, quantity, total_price) " + "VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                for (Cupcakes orderItem : cart) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, orderItem.getBottom().getId());
                    ps.setInt(3, orderItem.getTopping().getId());
                    ps.setInt(4, orderItem.getQuantity());
                    ps.setFloat(5, orderItem.getTotalCupcakePrice());
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected == 0) {
                        System.out.println("Order could not be added");
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not get orders from database");
        }
    }

    public ArrayList<Order> getListOfAllCustomersOrders(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT customer_orders.order_id, status_name, order_date, SUM(total_price) as total_price_sum " + "FROM customer_orders JOIN order_status ON customer_orders.status_id = order_status.status_id " + "JOIN customer_order_history ON customer_orders.order_id = customer_order_history.order_id " + "GROUP BY customer_orders.order_id, status_name, order_date " + "ORDER BY order_date DESC";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ArrayList<Order> listOfAllCustomersOrders = new ArrayList<>();
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    String status = rs.getString("status_name");
                    Date orderDate = rs.getDate("order_date");
                    float totalPrice = rs.getFloat("total_price_sum");

                    listOfAllCustomersOrders.add(new Order(orderId, status, orderDate, totalPrice));
                }
                return listOfAllCustomersOrders;
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not fetch email from database.");
        }
    }

    public ArrayList<Order> getListOfOneCustomersOrders(ConnectionPool connectionPool, int customerId) throws DatabaseException {
        String sql = "SELECT customer_orders.order_id, status_name, order_date, SUM(total_price) as total_price_sum FROM customer_orders JOIN order_status ON customer_orders.status_id = order_status.status_id JOIN customer_order_history ON customer_orders.order_id = customer_order_history.order_id WHERE customer_orders.customer_id = ? GROUP BY customer_orders.order_id, status_name, order_date ORDER BY order_date DESC";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, customerId);
                ArrayList<Order> listOfOneCustomersOrders = new ArrayList<>();
                ResultSet rs = ps.executeQuery();
                int orderId = 1;
                while (rs.next()) {
                    String status = rs.getString("status_name");
                    Date orderDate = rs.getDate("order_date");
                    float totalPrice = rs.getFloat("total_price_sum");

                    listOfOneCustomersOrders.add(new Order(orderId, status, orderDate, totalPrice));
                    orderId++;
                }
                return listOfOneCustomersOrders;
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not fetch email from database.");
        }
    }

    public void executeConfirmOrder(ConnectionPool connectionPool, int customerId, ArrayList<Cupcakes> cart) throws DatabaseException {
        int orderId = addNewOrder(connectionPool, customerId);
        System.out.println("Order ID: " + orderId);
        if (orderId != 0) {
            addNewOrderHistories(connectionPool, customerId, orderId, cart);
        }
    }

    public void removeOrderById(ConnectionPool connectionPool, int orderId) throws DatabaseException {
        String sql = "DELETE FROM customer_orders WHERE order_id = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DatabaseException(null, "No order found with ID: " + orderId);
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not delete order from database");
        }
    }

    public void removeOrderHistoryById(ConnectionPool connectionPool, int orderId) throws DatabaseException {
        String sql = "DELETE FROM customer_order_history WHERE order_id = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DatabaseException(null, "No order found with ID: " + orderId);
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not delete order history from database");
        }
    }

    public void executeRemoveOrderAndHistoryById(ConnectionPool connectionPool, int orderId) throws DatabaseException {
        removeOrderHistoryById(connectionPool, orderId);
        removeOrderById(connectionPool, orderId);
    }
}

