package app.persistence;

import app.entities.Bottom;
import app.entities.Topping;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM cupcake_bottoms";
        List<Bottom> bottoms = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int bottomId = rs.getInt("bottom_id");
                String flavour = rs.getString("bottom_flavour");

                bottoms.add(new Bottom(flavour, bottomId));
            }

            if (bottoms.isEmpty()) {
                throw new DatabaseException(null, "No bottoms found in database");
            }

            return bottoms;

        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not retrieve bottoms from database");
        }
    }

    public static List<Topping> getAllToppings(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM cupcake_toppings";
        List<Topping> toppings = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int bottomId = rs.getInt("topping_id");
                String flavour = rs.getString("topping_flavour");
                String imgName = rs.getString("img_name");

                toppings.add(new Topping(flavour, bottomId, imgName));
            }

            if (toppings.isEmpty()) {
                throw new DatabaseException(null, "No bottoms found in database");
            }

            return toppings;

        } catch (SQLException ex) {
            throw new DatabaseException(ex, "Could not retrieve bottoms from database");
        }
    }



    public float executeGetTotalCupcakePrice(ConnectionPool connectionPool, int toppingId, int bottomId) throws DatabaseException {
        float toppingPrice = getCupcakeToppingPriceFromToppingId(connectionPool, toppingId);
        float bottomPrice = getCupcakeBottomPriceFromBottomId(connectionPool, bottomId);
        float totalPrice = toppingPrice + bottomPrice;
        return totalPrice;
    }


}
