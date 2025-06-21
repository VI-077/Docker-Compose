CREATE DATABASE IF NOT EXISTS AutoPartShop;
USE AutoPartShop;

CREATE TABLE Manufacturer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) unique,
    country VARCHAR(100),
    address VARCHAR(255),
    phone VARCHAR(50),
    fax VARCHAR(100)
);

CREATE TABLE Car (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(100),
    model VARCHAR(100),
    production_year VARCHAR(4)
);

CREATE TABLE PartCategory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE
);

CREATE TABLE Part (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    code VARCHAR(50),
    buy_price DECIMAL(10,2),
    sell_price DECIMAL(10,2),
    manufacturer_id INT,
    category_id INT,
    FOREIGN KEY (manufacturer_id) REFERENCES Manufacturer(id),
    FOREIGN KEY (category_id) REFERENCES PartCategory(id)
);

CREATE TABLE Part_Car (
    part_id INT,
    car_id INT,
    PRIMARY KEY (part_id, car_id),
    FOREIGN KEY (part_id) REFERENCES Part(id),
    FOREIGN KEY (car_id) REFERENCES Car(id)
);

INSERT INTO PartCategory (name) VALUES
('ENGINE'), ('TIRES'), ('EXHAUST'), ('SUSPENSION'), ('BRAKES');




