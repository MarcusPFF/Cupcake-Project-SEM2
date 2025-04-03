package app.persistence;

import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CupcakeMapper {

    public String getBottomFlavourFromBottomId(ConnectionPool connectionPool, int bottomId) throws DatabaseException {
        String sql = "SELECT bottom_flavour FROM cupcake_bottoms WHERE bottom_id = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, bottomId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String bottomFlavour = rs.getString("bottom_flavour");
                    return bottomFlavour;
                } else {
                    throw new DatabaseException(null, "Could not find bottomFlavour for the given flavourId.");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not fetch bottomFlavour from database.");
        }
    }

    public String getToppingFlavourFromToppingId(ConnectionPool connectionPool, int toppingId) throws DatabaseException {
        String sql = "SELECT topping_flavour FROM cupcake_toppings WHERE topping_id = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, toppingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String toppingFlavour = rs.getString("topping_flavour");
                    return toppingFlavour;
                } else {
                    throw new DatabaseException(null, "Could not find toppingFlavour for the given flavourId.");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not fetch toppingFlavour from database.");
        }
    }


    public float getCupcakeBottomPriceFromBottomId(ConnectionPool connectionPool, int bottomId) throws DatabaseException {
        String sql = "SELECT bottom_price FROM cupcake_bottoms WHERE bottom_id = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, bottomId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    float bottomPrice = rs.getFloat("bottom_price");
                    return bottomPrice;
                } else {
                    System.out.println(bottomId);
                    throw new DatabaseException(null, "Could not find bottomPrice for the given flavourId.");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not fetch bottomPrice from database.");
        }
    }

    public float getCupcakeToppingPriceFromToppingId(ConnectionPool connectionPool, int toppingId) throws DatabaseException {
        String sql = "SELECT topping_price FROM cupcake_toppings WHERE topping_id = ?;";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, toppingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    float toppingPrice = rs.getFloat("topping_price");
                    return toppingPrice;
                } else {
                    throw new DatabaseException(null, "Could not find toppingPrice for the given flavourId.");
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not fetch toppingPrice from database.");
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
        String sql = "SELECT bottom_id FROM cupcake_bottoms WHERE bottom_flavour = ?;";

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

    public float executeGetTotalCupcakePrice(ConnectionPool connectionPool, int toppingId, int bottomId) throws DatabaseException {
        float toppingPrice = getCupcakeToppingPriceFromToppingId(connectionPool, toppingId);
        float bottomPrice = getCupcakeBottomPriceFromBottomId(connectionPool, bottomId);
        float totalPrice = toppingPrice + bottomPrice;
        return totalPrice;
    }


}
