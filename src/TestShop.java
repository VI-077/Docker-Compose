import java.sql.SQLException;
import java.util.List;

public class TestShop {
    public static void main(String[] args) {
        try {
            Manufacturer manufacturer1 = DBUtil.findManufacturerByName("Bosch");
            if (manufacturer1 == null) {
                manufacturer1 = new Manufacturer();
                manufacturer1.setName("Bosch");
                manufacturer1.setCountry("Germany");
                manufacturer1.setAddress("Street 1");
                manufacturer1.setPhone("12345");
                manufacturer1.setFax("54321");
                DBUtil.insertManufacturer(manufacturer1);
            }

            Manufacturer manufacturer2 = DBUtil.findManufacturerByName("Valeo");
            if (manufacturer2 == null) {
                manufacturer2 = new Manufacturer();
                manufacturer2.setName("Valeo");
                manufacturer2.setCountry("France");
                manufacturer2.setAddress("Avenue de Paris 10");
                manufacturer2.setPhone("98765");
                manufacturer2.setFax("56789");
                DBUtil.insertManufacturer(manufacturer2);
            }

            Manufacturer manufacturer3 = DBUtil.findManufacturerByName("Denso");
            if (manufacturer3 == null) {
                manufacturer3 = new Manufacturer();
                manufacturer3.setName("Denso");
                manufacturer3.setCountry("Japan");
                manufacturer3.setAddress("Tokyo Central 5");
                manufacturer3.setPhone("55555");
                manufacturer3.setFax("88888");
                DBUtil.insertManufacturer(manufacturer3);
            }

            // Categories (repeat for each needed)
            String[] neededCategories = {"ENGINE", "TIRES", "EXHAUST", "SUSPENSION", "BRAKES"};
            for (String catName : neededCategories) {
                PartCategory cat = DBUtil.findCategoryByName(catName);
                if (cat == null) {
                    cat = new PartCategory();
                    cat.setName(catName);
                    DBUtil.insertPartCategory(cat);
                }
            }
            // Now fetch categories for use below
            PartCategory engineCategory = DBUtil.findCategoryByName("ENGINE");
            PartCategory tiresCategory = DBUtil.findCategoryByName("TIRES");
            PartCategory exhaustCategory = DBUtil.findCategoryByName("EXHAUST");
            PartCategory suspensionCategory = DBUtil.findCategoryByName("SUSPENSION");
            PartCategory brakesCategory = DBUtil.findCategoryByName("BRAKES");

            // Parts
            if (DBUtil.findPartByCodeOrName("ALT123") == null) {
                Part part = new Part();
                part.setName("Alternator");
                part.setCode("ALT123");
                part.setBuyPrice(120.5);
                part.setSellPrice(180.75);
                part.setManufacturer(manufacturer1);
                part.setCategory(engineCategory);
                DBUtil.insertPart(part);
            }

            if (DBUtil.findPartByCodeOrName("TIR001") == null) {
                Part part1 = new Part();
                part1.setName("All-Season Tire");
                part1.setCode("TIR001");
                part1.setBuyPrice(45.0);
                part1.setSellPrice(70.0);
                part1.setManufacturer(manufacturer1);
                part1.setCategory(tiresCategory);
                DBUtil.insertPart(part1);
            }

            if (DBUtil.findPartByCodeOrName("EXH555") == null) {
                Part part2 = new Part();
                part2.setName("Performance Exhaust Pipe");
                part2.setCode("EXH555");
                part2.setBuyPrice(80.0);
                part2.setSellPrice(125.0);
                part2.setManufacturer(manufacturer2);
                part2.setCategory(exhaustCategory);
                DBUtil.insertPart(part2);
            }

            if (DBUtil.findPartByCodeOrName("BRK900") == null) {
                Part part3 = new Part();
                part3.setName("Ceramic Brake Pads");
                part3.setCode("BRK900");
                part3.setBuyPrice(32.5);
                part3.setSellPrice(58.0);
                part3.setManufacturer(manufacturer3);
                part3.setCategory(brakesCategory);
                DBUtil.insertPart(part3);
            }

            // The rest of your demo code...
            Shop shop = new Shop();
            List<Part> allParts = DBUtil.getAllParts();
            for (Part p : allParts) {
                shop.addPart(p);
            }

            List<Part> inventory = shop.getInventory();
            for (Part p : inventory) {
                System.out.println("Part ID: " + p.getId() +
                        ", Name: " + p.getName() +
                        ", Manufacturer: " + p.getManufacturer().getName() +
                        ", Category: " + p.getCategory().getName() +
                        ", Buy: " + p.getBuyPrice() +
                        ", Sell: " + p.getSellPrice());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
