package app.entities;

import org.postgresql.jdbc2.optional.ConnectionPool;
import app.persistence.CupcakeMapper;
import java.util.ArrayList;

public class Cupcakes {
    private String bottomFlavour;
    private String toppingFlavour;
    private int quantity;
    private float totalCupcakePrice;

    public Cupcakes( String bottomFlavour, String toppingFlavour, int quantity, float totalCupcakePrice) {
        this.bottomFlavour = bottomFlavour;
        this.toppingFlavour = toppingFlavour;
        this.quantity = quantity;
        this.totalCupcakePrice = totalCupcakePrice;
    }




    public String getBottomFlavour() {
        return bottomFlavour;
    }

    public void setBottomFlavour(String bottomFlavour) {
        this.bottomFlavour = bottomFlavour;
    }

    public String getToppingFlavour() {
        return toppingFlavour;
    }

    public void setToppingFlavour(String toppingFlavour) {
        this.toppingFlavour = toppingFlavour;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalCupcakePrice() {
        return totalCupcakePrice;
    }

    public void setTotalCupcakePrice(float totalCupcakePrice) {
        this.totalCupcakePrice = totalCupcakePrice;
    }


    void executeAddCupcakeToCart(ConnectionPool connectionPool, int bottomId, int toppingId, int quantity, ArrayList<Cupcakes> cart){
        float totalPriceOfOneCupcake = (executeGetTotalCupcakePrice(connectionPool, bottomId, toppingId));
        float totalPrice = totalPriceOfOneCupcake * quantity;
        String bottomFlavour = executeGetBottomFlavourFromBottomId(connectionPool, bottomId);
        String toppingFlavour = executeGetToppingFlavourFromToppingId(connectionPool, toppingId);
        cart.add(new Cupcakes(bottomFlavour, toppingFlavour, quantity, totalPrice));
    }
}
