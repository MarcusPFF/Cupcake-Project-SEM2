package app.entities;

public class Order {
    private int orderHistoryId;
    private int orderId;
    private int bottomId;
    private int toppingId;
    private int quantity;
    private float price;

    public Order(int orderHistoryId, int orderId, int bottomId, int toppingId, int quantity, float price) {
        this.orderHistoryId = orderHistoryId;
        this.orderId = orderId;
        this.bottomId = bottomId;
        this.toppingId = toppingId;
        this.quantity = quantity;
        this.price = price;
    }

    public Order(int bottomId, int toppingId, int quantity, float price) {
        this.bottomId = bottomId;
        this.toppingId = toppingId;
        this.quantity = quantity;
        this.price = price;
    }


}
