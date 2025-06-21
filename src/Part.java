public class Part {
    private int id;
    private String name;
    private String code;
    private double buyPrice;
    private double sellPrice;
    private Manufacturer manufacturer;
    private PartCategory category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public PartCategory getCategory() {
        return category;
    }

    public void setCategory(PartCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
