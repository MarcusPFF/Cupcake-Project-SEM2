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

class OrderMapperTest2 {

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
        -- Drop unnecessary tables if they exist
        DROP TABLE IF EXISTS customer_orders CASCADE;
        DROP TABLE IF EXISTS customer_order_history CASCADE;
        DROP TABLE IF EXISTS order_status CASCADE;
        DROP TABLE IF EXISTS cupcake_bottoms CASCADE;
        DROP TABLE IF EXISTS cupcake_toppings CASCADE;

        -- Create necessary tables
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

        CREATE TABLE cupcake_bottoms (
            bottom_id SERIAL PRIMARY KEY,
            bottom_flavour VARCHAR(255),
            bottom_price FLOAT
        );

        CREATE TABLE cupcake_toppings (
            topping_id SERIAL PRIMARY KEY,
            topping_flavour VARCHAR(255),
            topping_price FLOAT
        );

        INSERT INTO cupcake_bottoms (bottom_flavour, bottom_price) 
        VALUES ('Vanilla', 10.0);

        INSERT INTO cupcake_toppings (topping_flavour, topping_price) 
        VALUES ('Chocolate', 5.0);

        INSERT INTO order_status (status_name) 
        VALUES ('Pending');

        INSERT INTO customer_orders (customer_id, order_date, status_id) 
        VALUES (1, CURRENT_DATE, 1) 
        RETURNING order_id;

        INSERT INTO customer_order_history (order_id, bottom_id, topping_id, quantity, total_price) 
        VALUES (1, 1, 1, 2, 30.0);
        """);
        }
    }



    @Test
    void testGetListOfAllCustomersOrders_Success() throws DatabaseException {
        List<Order> orders = orderMapper.getListOfAllCustomersOrders(connectionPool);
        assertTrue(orders.size() > 0);
    }

    @Test
    void testRemoveOrderHistoryById_Success() throws DatabaseException {
        Bottom bottom = new Bottom("Vanilla", 1);
        Topping topping = new Topping("Chocolate", 1);
        ArrayList<Cupcakes> cart = new ArrayList<>();
        cart.add(new Cupcakes(bottom, topping, 2, 15.0f));

        int orderId = orderMapper.addNewOrder(connectionPool, 1);
        orderMapper.addNewOrderHistories(connectionPool, 1, orderId, cart);
        orderMapper.removeOrderHistoryById(connectionPool, orderId);

        List<Order> orders = orderMapper.getListOfOneCustomersOrders(connectionPool, 1);
        assertTrue(orders.size() > 0);
    }

}
