package app.entities;

public class Customer {
    private final float wallet;
    private String email;
    private String username;
    private String password;
    private int customerId;

    public Customer(int customerId, String email, String username, String password, float wallet) {
        this.customerId = customerId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.wallet = wallet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LifeHackTeam3Subscriber{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
