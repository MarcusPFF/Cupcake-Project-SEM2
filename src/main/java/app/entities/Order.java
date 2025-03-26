package app.entities;

public class Order {
    private int orderId;
    private int bottomId;
    private int toppingId;
    private int quantity;
    private int totalPrice;

    public Order(int orderId, int bottomId, int toppingId, int quantity, int price) {
        this.orderId = orderId;
        this.bottomId = bottomId;
        this.toppingId = toppingId;
        this.quantity = quantity;
        this.totalPrice = price;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBottomId() {
        return bottomId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    public int getToppingId() {
        return toppingId;
    }

    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
