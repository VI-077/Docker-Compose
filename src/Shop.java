import java.util.ArrayList;
import java.util.List;

public class Shop {
    private List<Part> inventory;

    public Shop() {
        inventory = new ArrayList<>();
    }

    public void addPart(Part part) {
        inventory.add(part);
    }

    public List<Part> getInventory() {
        return inventory;
    }

    public Part findPartById(int id) {
        for (Part part : inventory) {
            if (part.getId() == id) {
                return part;
            }
        }
        return null;
    }

    public List<Part> findPartsByManufacturer(int manufacturerId) {
        List<Part> result = new ArrayList<>();
        for (Part part : inventory) {
            if (part.getManufacturer() != null && part.getManufacturer().getId() == manufacturerId) {
                result.add(part);
            }
        }
        return result;
    }

    public List<Part> findPartsByCategory(int categoryId) {
        List<Part> result = new ArrayList<>();
        for (Part part : inventory) {
            if (part.getCategory() != null && part.getCategory().getId() == categoryId) {
                result.add(part);
            }
        }
        return result;
    }
}
