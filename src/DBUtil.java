import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    private static final String HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String PORT = System.getenv().getOrDefault("DB_PORT", "3306");
    private static final String DATABASE = System.getenv().getOrDefault("DB_NAME", "AutoPartShop");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "root");
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void insertManufacturer(Manufacturer manufacturer) throws SQLException {
        String sql = "INSERT INTO Manufacturer (name, country, address, phone, fax) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, manufacturer.getName());
            stmt.setString(2, manufacturer.getCountry());
            stmt.setString(3, manufacturer.getAddress());
            stmt.setString(4, manufacturer.getPhone());
            stmt.setString(5, manufacturer.getFax());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    manufacturer.setId(keys.getInt(1));
                }
            }
        }
    }

    public static List<Manufacturer> getAllManufacturers() throws SQLException {
        List<Manufacturer> list = new ArrayList<>();
        String sql = "SELECT * FROM Manufacturer";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Manufacturer m = new Manufacturer();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setCountry(rs.getString("country"));
                m.setAddress(rs.getString("address"));
                m.setPhone(rs.getString("phone"));
                m.setFax(rs.getString("fax"));
                list.add(m);
            }
        }
        return list;
    }

    public static void insertPartCategory(PartCategory category) throws SQLException {
        String sql = "INSERT INTO PartCategory (name) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    category.setId(keys.getInt(1));
                }
            }
        }
    }

    public static List<PartCategory> getAllCategories() throws SQLException {
        List<PartCategory> categories = new ArrayList<>();
        String sql = "SELECT * FROM PartCategory";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PartCategory c = new PartCategory();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                categories.add(c);
            }
        }
        return categories;
    }

    public static void insertPart(Part part) throws SQLException {
        String sql = "INSERT INTO Part (name, code, buy_price, sell_price, manufacturer_id, category_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, part.getName());
            stmt.setString(2, part.getCode());
            stmt.setDouble(3, part.getBuyPrice());
            stmt.setDouble(4, part.getSellPrice());
            stmt.setInt(5, part.getManufacturer().getId());
            stmt.setInt(6, part.getCategory().getId());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    part.setId(keys.getInt(1));
                }
            }
        }
    }

    public static List<Part> getAllParts() throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.code, p.buy_price, p.sell_price, " +
                "m.id AS man_id, m.name AS man_name, m.country AS man_country, m.address AS man_address, m.phone AS man_phone, m.fax AS man_fax, " +
                "c.id AS cat_id, c.name AS cat_name " +
                "FROM Part p " +
                "LEFT JOIN Manufacturer m ON p.manufacturer_id = m.id " +
                "LEFT JOIN PartCategory c ON p.category_id = c.id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Part part = new Part();
                part.setId(rs.getInt("id"));
                part.setName(rs.getString("name"));
                part.setCode(rs.getString("code"));
                part.setBuyPrice(rs.getDouble("buy_price"));
                part.setSellPrice(rs.getDouble("sell_price"));

                Manufacturer m = new Manufacturer();
                m.setId(rs.getInt("man_id"));
                m.setName(rs.getString("man_name"));
                m.setCountry(rs.getString("man_country"));
                m.setAddress(rs.getString("man_address"));
                m.setPhone(rs.getString("man_phone"));
                m.setFax(rs.getString("man_fax"));
                part.setManufacturer(m);

                PartCategory cat = new PartCategory();
                cat.setId(rs.getInt("cat_id"));
                cat.setName(rs.getString("cat_name"));
                part.setCategory(cat);

                parts.add(part);
            }
        }
        return parts;
    }

    public static void insertCar(Car car) throws SQLException {
        String sql = "INSERT INTO Car (brand, model, production_year) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setString(3, String.valueOf(car.getProductionYear()));
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    car.setId(keys.getInt(1));
                }
            }
        }
    }

    public static List<Car> getAllCars() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM Car";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Car car = new Car();
                car.setId(rs.getInt("id"));
                car.setBrand(rs.getString("brand"));
                car.setModel(rs.getString("model"));
                car.setProductionYear(Integer.parseInt(rs.getString("production_year")));
                cars.add(car);
            }
        }
        return cars;
    }

    public static PartCategory findCategoryByName(String name) throws SQLException {
        String sql = "SELECT * FROM PartCategory WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PartCategory cat = new PartCategory();
                    cat.setId(rs.getInt("id"));
                    cat.setName(rs.getString("name"));
                    return cat;
                }
            }
        }
        return null;
    }

    public static void updatePart(Part part) throws SQLException {
        String sql = "UPDATE Part SET name=?, code=?, buy_price=?, sell_price=?, manufacturer_id=?, category_id=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, part.getName());
            stmt.setString(2, part.getCode());
            stmt.setDouble(3, part.getBuyPrice());
            stmt.setDouble(4, part.getSellPrice());
            stmt.setInt(5, part.getManufacturer().getId());
            stmt.setInt(6, part.getCategory().getId());
            stmt.setInt(7, part.getId());
            stmt.executeUpdate();
        }
    }

    public static void deletePart(int id) throws SQLException {
        String sql = "DELETE FROM Part WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public static Part findPartByCodeOrName(String s) throws SQLException {
        String sql = "SELECT p.*, m.id as man_id, m.name as man_name, m.country, m.address, m.phone, m.fax, " +
                "c.id as cat_id, c.name as cat_name " +
                "FROM Part p " +
                "LEFT JOIN Manufacturer m ON p.manufacturer_id = m.id " +
                "LEFT JOIN PartCategory c ON p.category_id = c.id " +
                "WHERE p.code = ? OR p.name = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s);
            stmt.setString(2, s);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Part part = new Part();
                    part.setId(rs.getInt("id"));
                    part.setName(rs.getString("name"));
                    part.setCode(rs.getString("code"));
                    part.setBuyPrice(rs.getDouble("buy_price"));
                    part.setSellPrice(rs.getDouble("sell_price"));

                    Manufacturer m = new Manufacturer();
                    m.setId(rs.getInt("man_id"));
                    m.setName(rs.getString("man_name"));
                    m.setCountry(rs.getString("country"));
                    m.setAddress(rs.getString("address"));
                    m.setPhone(rs.getString("phone"));
                    m.setFax(rs.getString("fax"));
                    part.setManufacturer(m);

                    PartCategory cat = new PartCategory();
                    cat.setId(rs.getInt("cat_id"));
                    cat.setName(rs.getString("cat_name"));
                    part.setCategory(cat);

                    return part;
                }
            }
        }
        return null;
    }

    public static Part getPartById(int id) throws SQLException {
        String sql = "SELECT p.*, m.id as man_id, m.name as man_name, m.country, m.address, m.phone, m.fax, " +
                "c.id as cat_id, c.name as cat_name " +
                "FROM Part p " +
                "LEFT JOIN Manufacturer m ON p.manufacturer_id = m.id " +
                "LEFT JOIN PartCategory c ON p.category_id = c.id " +
                "WHERE p.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Part part = new Part();
                    part.setId(rs.getInt("id"));
                    part.setName(rs.getString("name"));
                    part.setCode(rs.getString("code"));
                    part.setBuyPrice(rs.getDouble("buy_price"));
                    part.setSellPrice(rs.getDouble("sell_price"));

                    Manufacturer m = new Manufacturer();
                    m.setId(rs.getInt("man_id"));
                    m.setName(rs.getString("man_name"));
                    m.setCountry(rs.getString("country"));
                    m.setAddress(rs.getString("address"));
                    m.setPhone(rs.getString("phone"));
                    m.setFax(rs.getString("fax"));
                    part.setManufacturer(m);

                    PartCategory cat = new PartCategory();
                    cat.setId(rs.getInt("cat_id"));
                    cat.setName(rs.getString("cat_name"));
                    part.setCategory(cat);

                    return part;
                }
            }
        }
        return null;
    }

    public static Manufacturer findManufacturerByName(String name) throws SQLException {
        String sql = "SELECT * FROM Manufacturer WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Manufacturer m = new Manufacturer();
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setCountry(rs.getString("country"));
                    m.setAddress(rs.getString("address"));
                    m.setPhone(rs.getString("phone"));
                    m.setFax(rs.getString("fax"));
                    return m;
                }
            }
        }
        return null;
    }

}
