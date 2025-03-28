package app.entities;

import java.util.Date;

public class Order {
    private int orderId;
    private String status;
    private Date date;
    private float totalOrderPrice;

    public Order(int orderId, String status, Date date, float totalOrderPrice) {
        this.orderId = orderId;
        this.status = status;
        this.date = date;
        this.totalOrderPrice = totalOrderPrice;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(float totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }
}
