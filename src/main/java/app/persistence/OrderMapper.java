package app.persistence;

import app.exceptions.DatabaseException;
import org.postgresql.jdbc2.optional.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;


public class OrderMapper {



    public int addNewOrder(ConnectionPool connectionPool, int customerId) throws DatabaseException {
        String sql = "INSERT INTO customer_orders(customer_id, order_date, status_id) " + "VALUES (?, ?, ?);";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, customerId);
                ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                ps.setInt(3, 1);
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
        return 0;
    }

    public int addNewOrderHistorys(ConnectionPool connectionPool, int customerId, int orderId, List<Order> order) throws DatabaseException {

        String sql = "INSERT INTO customer_order_history(customer_id, order_id, bottom_id, topping_id, quantity, total_price) " + "VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                for (Order orderItem : order) {
                    ps.setInt(1, customerId);
                    ps.setInt(2, orderId);
                    ps.setInt(3, Order.getBottomId());
                    ps.setInt(4, Order.getToppingId());
                    ps.setInt(5, Order.getQuantity());
                    ps.setInt(6, Order.getTotalPrice());
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected == 0) {
                        System.out.println("Order could not be added");
                    }
                }

            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not get orders from database");
        }
        return;
    }

}
