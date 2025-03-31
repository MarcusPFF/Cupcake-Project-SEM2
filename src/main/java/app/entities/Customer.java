package app.entities;

public class Customer {
    private final float wallet;
    private String email;
    private String name;
    private String password;
    private int customerId;

    public Customer(int customerId, String email, String name, String password, float wallet) {
        this.customerId = customerId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.wallet = wallet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getWallet() {
        return wallet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "wallet=" + wallet +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", customerId=" + customerId +
                '}';
    }
}
