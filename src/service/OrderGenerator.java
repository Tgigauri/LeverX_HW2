package service;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.Order;
import model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Data
@AllArgsConstructor
public class OrderGenerator {

    private final List<Product> catalog;

    public Order generateOrder(int customerId, int orderId) {
        Map<Product, Integer> productsInOrder = new HashMap<>();

        int numItems = Math.min(ThreadLocalRandom.current().nextInt(1, 6), catalog.size());

        for (int i = 0; i < numItems; i++) {
            Product product = catalog.get(ThreadLocalRandom.current().nextInt(catalog.size()));

            int quantity = ThreadLocalRandom.current().nextInt(1, 6);

            productsInOrder.merge(product, quantity, Integer::sum);
        }

        double totalPrice = productsInOrder.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();

        return new Order(orderId, customerId, productsInOrder, totalPrice, null);
    }
}
