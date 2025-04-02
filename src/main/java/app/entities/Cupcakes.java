package app.entities;

public class Cupcakes {
    private Bottom bottom;
    private Topping topping;
    private int quantity;
    private float totalCupcakePrice;

    public Cupcakes(Bottom bottom, Topping topping, int quantity, float totalCupcakePrice) {
        this.bottom = bottom;
        this.topping = topping;
        this.quantity = quantity;
        this.totalCupcakePrice = totalCupcakePrice;
    }

    public Bottom getBottom() {
        return bottom;
    }

    public void setBottom(Bottom bottom) {
        this.bottom = bottom;
    }

    public Topping getTopping() {
        return topping;
    }

    public void setTopping(Topping topping) {
        this.topping = topping;
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

    @Override
    public String toString() {
        return "Cupcakes{" +
                "bottomFlavour='" + bottom + '\'' +
                ", toppingFlavour='" + topping + '\'' +
                ", quantity=" + quantity +
                ", totalCupcakePrice=" + totalCupcakePrice +
                '}';
    }
}
