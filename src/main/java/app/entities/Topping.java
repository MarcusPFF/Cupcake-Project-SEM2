package app.entities;

public class Topping {
    private String flavour;
    private int id;
    private String imgName;

    public Topping(String flavour, int id, String imgName) {
        this.flavour = flavour;
        this.id = id;
        this.imgName = imgName;
    }

    public Topping(String flavour, int id) {
        this.flavour = flavour;
        this.id = id;
    }

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

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    @Override
    public String toString() {
        return "Topping{" +
                "flavour='" + flavour + '\'' +
                ", id=" + id +
                ", imgName='" + imgName + '\'' +
                '}';
    }
}
