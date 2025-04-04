package app.persistence;

import app.entities.Cupcakes;
import app.entities.Order;
import app.entities.Bottom;
import app.entities.Topping;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private static ConnectionPool connectionPool;
    private static OrderMapper orderMapper;

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
    void setUp() {
        orderMapper = new OrderMapper();
    }

    private static void setupTestDatabase() throws SQLException {
        try (Connection conn = connectionPool.getConnection()) {
            conn.createStatement().execute("""
            DROP TABLE IF EXISTS customer_orders CASCADE;
            DROP TABLE IF EXISTS customer_order_history CASCADE;
            DROP TABLE IF EXISTS order_status CASCADE;

            CREATE TABLE customer_orders (
                order_id SERIAL PRIMARY KEY,
                customer_id INT,
                order_date DATE,
                status_id INT
            );

            CREATE TABLE customer_order_history (
                order_id INT REFERENCES customer_orders(order_id),
                bottom_id INT,
                topping_id INT,
                quantity INT,
                total_price FLOAT
            );

            CREATE TABLE order_status (
                status_id SERIAL PRIMARY KEY,
                status_name VARCHAR(255)
            );

            INSERT INTO order_status (status_name) VALUES ('Pending');
            INSERT INTO customer_orders (customer_id, order_date, status_id) VALUES (1, CURRENT_DATE, 1);
            """);
        }
    }

    @Test
    void testAddNewOrder_Success() throws DatabaseException {
        int orderId = orderMapper.addNewOrder(connectionPool, 1);
        assertTrue(orderId > 0);
    }

    @Test
    void testAddNewOrderHistories_Success() throws DatabaseException {
        Bottom bottom = new Bottom("Vanilla", 1);
        Topping topping = new Topping("Chocolate", 1);
        ArrayList<Cupcakes> cart = new ArrayList<>();
        cart.add(new Cupcakes(bottom, topping, 2, 15.0f));

        int orderId = orderMapper.addNewOrder(connectionPool, 1);
        orderMapper.addNewOrderHistories(connectionPool, 1, orderId, cart);

        List<Order> orders = orderMapper.getListOfOneCustomersOrders(connectionPool, 1);
        assertEquals(1, orders.size());
    }

    @Test
    void testGetListOfOneCustomersOrders_Success() throws DatabaseException {
        List<Order> orders = orderMapper.getListOfOneCustomersOrders(connectionPool, 1);
        assertTrue(orders.size() > 0);
    }

    @Test
    void testExecuteConfirmOrder_Success() throws DatabaseException {
        Bottom bottom = new Bottom("Vanilla", 1);
        Topping topping = new Topping("Chocolate", 1);
        ArrayList<Cupcakes> cart = new ArrayList<>();
        cart.add(new Cupcakes(bottom, topping, 2, 15.0f));

        orderMapper.executeConfirmOrder(connectionPool, 1, cart);

        List<Order> orders = orderMapper.getListOfOneCustomersOrders(connectionPool, 1);
        assertTrue(orders.size() > 0);
    }

    @Test
    void testExecuteRemoveOrderAndHistoryById_Success() throws DatabaseException {
        Bottom bottom = new Bottom("Vanilla", 1);
        Topping topping = new Topping("Chocolate", 1);
        ArrayList<Cupcakes> cart = new ArrayList<>();
        cart.add(new Cupcakes(bottom, topping, 2, 15.0f));

        int orderId = orderMapper.addNewOrder(connectionPool, 1);
        orderMapper.addNewOrderHistories(connectionPool, 1, orderId, cart);
        orderMapper.executeRemoveOrderAndHistoryById(connectionPool, orderId);

        List<Order> orders = orderMapper.getListOfOneCustomersOrders(connectionPool, 1);
        assertTrue(orders.isEmpty());
    }
}
