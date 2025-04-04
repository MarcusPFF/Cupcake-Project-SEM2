package app.persistence;

import app.exceptions.DatabaseException;
import app.entities.Bottom;
import app.entities.Topping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CupcakeMapperTest {

    private static ConnectionPool connectionPool;
    private static CupcakeMapper cupcakeMapper;

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
        cupcakeMapper = new CupcakeMapper();
    }

    private static void setupTestDatabase() throws SQLException {
        try (Connection conn = connectionPool.getConnection()) {
            conn.createStatement().execute("""
            DROP TABLE IF EXISTS customer_order_history CASCADE;
            DROP TABLE IF EXISTS cupcake_bottoms CASCADE;
            DROP TABLE IF EXISTS cupcake_toppings CASCADE;

            CREATE TABLE cupcake_bottoms (
                bottom_id SERIAL PRIMARY KEY,
                bottom_flavour VARCHAR(255),
                bottom_price FLOAT
            );

            CREATE TABLE cupcake_toppings (
                topping_id SERIAL PRIMARY KEY,
                topping_flavour VARCHAR(255),
                topping_price FLOAT,
                img_name VARCHAR(255)
            );

            CREATE TABLE customer_order_history (
                order_id SERIAL PRIMARY KEY,
                bottom_id INT REFERENCES cupcake_bottoms(bottom_id) ON DELETE CASCADE,
                topping_id INT REFERENCES cupcake_toppings(topping_id) ON DELETE CASCADE
            );

            INSERT INTO cupcake_bottoms (bottom_flavour, bottom_price) VALUES ('Vanilla', 10.0);
            INSERT INTO cupcake_toppings (topping_flavour, topping_price, img_name) VALUES ('Chocolate', 5.0, 'chocolate.png');
        """);
        }
    }


    @Test
    void testGetBottomFlavourFromBottomId_Success() throws DatabaseException {
        String flavour = cupcakeMapper.getBottomFlavourFromBottomId(connectionPool, 1);
        assertEquals("Vanilla", flavour);
    }

    @Test
    void testGetToppingFlavourFromToppingId_Success() throws DatabaseException {
        String flavour = cupcakeMapper.getToppingFlavourFromToppingId(connectionPool, 1);
        assertEquals("Chocolate", flavour);
    }

    @Test
    void testGetCupcakeBottomPriceFromBottomId_Success() throws DatabaseException {
        float price = cupcakeMapper.getCupcakeBottomPriceFromBottomId(connectionPool, 1);
        assertEquals(10.0f, price);
    }

    @Test
    void testGetCupcakeToppingPriceFromToppingId_Success() throws DatabaseException {
        float price = cupcakeMapper.getCupcakeToppingPriceFromToppingId(connectionPool, 1);
        assertEquals(5.0f, price);
    }

    @Test
    void testGetAllBottoms_Success() throws DatabaseException {
        List<Bottom> bottoms = cupcakeMapper.getAllBottoms(connectionPool);
        assertEquals(1, bottoms.size());
        assertEquals("Vanilla", bottoms.get(0).getFlavour());
    }

    @Test
    void testGetAllToppings_Success() throws DatabaseException {
        List<Topping> toppings = cupcakeMapper.getAllToppings(connectionPool);
        assertEquals(1, toppings.size());
        assertEquals("Chocolate", toppings.get(0).getFlavour());
    }

    @Test
    void testExecuteGetTotalCupcakePrice() throws DatabaseException {
        float totalPrice = cupcakeMapper.executeGetTotalCupcakePrice(connectionPool, 1, 1);
        assertEquals(15.0f, totalPrice);
    }
}
