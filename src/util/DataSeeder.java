package util;

import lombok.experimental.UtilityClass;
import model.Product;
import service.Warehouse;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class DataSeeder {
    public List<Product> seedProducts() {
        List<Product> products = new ArrayList<>();

        products.add(new Product(1, "Laptop", 1200.00));
        products.add(new Product(2, "Smartphone", 800.00));
        products.add(new Product(3, "Headphones", 150.00));
        products.add(new Product(4, "Keyboard", 70.00));
        products.add(new Product(5, "Mouse", 50.00));
        products.add(new Product(6, "Monitor", 300.00));
        products.add(new Product(7, "USB Cable", 10.00));
        products.add(new Product(8, "External HDD", 100.00));

        return products;
    }

    public void seedWarehouse(Warehouse warehouse, List<Product> products, int initialQuantity) {
        for (Product product : products) {
            warehouse.add(product, initialQuantity);
        }
    }
}
