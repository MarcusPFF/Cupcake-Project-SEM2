package app.persistence;

import app.entities.Cupcakes;
import app.entities.Order;
import app.exceptions.DatabaseException;
import org.postgresql.jdbc2.optional.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderMapper {


    public int addNewOrder(ConnectionPool connectionPool, int customerId) throws DatabaseException {
        String sql = "INSERT INTO customer_orders(customer_id, order_date, status_id) " + "VALUES (?, CURRENT_DATE, 1);";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, customerId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("Order could not be added");
                }
                ResultSet rs = ps.executeQuery();
                return rs.getInt("order_id");
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not get orders from database");
        }
    }

    public void addNewOrderHistories(ConnectionPool connectionPool, int customerId, int orderId, ArrayList<Cupcakes> cart) throws DatabaseException {

        String sql = "INSERT INTO customer_order_history(customer_id, order_id, bottom_id, topping_id, quantity, total_price) " + "VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                for (Cupcakes orderItem : cart) {
                    ps.setInt(1, customerId);
                    ps.setInt(2, orderId);
                    ps.setString(3, orderItem.getBottomFlavour());
                    ps.setString(4, orderItem.getToppingFlavour());
                    ps.setInt(5, orderItem.getQuantity());
                    ps.setFloat(6, orderItem.getTotalCupcakePrice());
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
        String sql = "SELECT customer_orders.order_id, status_name, order_date, SUM(total_price) as total_price_sum " +
                "FROM customer_orders JOIN order_status ON customer_orders.status_id = order_status.order_id " +
                "JOIN customer_order_history ON customer_orders.order_id = customer_order_history.order_id " +
                "GROUP BY customer_orders.order_id, status_name, order_date " +
                "ORDER BY order_date DESC";

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
        String sql = "SELECT status_name, order_date, SUM(total_price) as total_price_sum FROM customer_orders JOIN order_status ON customer_orders.status_id = order_status.order_id JOIN customer_order_history ON customer_orders.order_id = customer_order_history.order_id GROUP BY customer_orders.order_id, status_name, order_date ORDER BY order_date DESC WHERE customer_id = " + customerId;

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
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
        addNewOrderHistories(connectionPool, customerId, orderId, cart);
    }

    public void removeOrderById(ConnectionPool connectionPool, int orderId) throws DatabaseException {
        String sql = "DELETE FROM customer_orders WHERE order_id = ?;";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

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

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DatabaseException(null, "No order found with ID: " + orderId);
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not delete order history from database");
        }
    }

    public void executeRemoveOrderAndHistoryById(ConnectionPool connectionPool, int orderId) throws
            DatabaseException {
        removeOrderHistoryById(connectionPool, orderId);
        removeOrderById(connectionPool, orderId);
    }
}

