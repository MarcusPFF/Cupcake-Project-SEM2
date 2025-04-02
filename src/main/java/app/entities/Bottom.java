package app.entities;

public class Bottom {
    private String flavour;
    private int id;

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bottom(String flavour, int id) {
        this.flavour = flavour;
        this.id = id;
    }
}
