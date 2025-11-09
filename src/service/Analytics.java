package service;

import lombok.AllArgsConstructor;
import model.Order;
import model.Product;
import util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Analytics {
    // List of all processed orders
    private final List<Order> processedOrders;


    public long getTotalOrders() {
        return processedOrders.size(); // Simply return the size of the list
    }


    public double getTotalProfit() {
        // Use a stream to sum all order prices
        return processedOrders.stream()
                .mapToDouble(Order::getTotalPrice) // Extract totalPrice from each order
                .sum();                             // Sum all prices
    }


    public List<Map.Entry<Product, Integer>> getTopBestSellingProducts(int topN) {

        // Map to store total quantity sold for each product
        Map<Product, Integer> productSales = new HashMap<>();

        // Loop through all orders
        for (Order order : processedOrders) {
            for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                // Add quantity to the productSales map (if not present, start with quantity)
                productSales.merge(product, quantity, Integer::sum);
            }
        }

        // Sort products by quantity sold in descending order and return top N
        return productSales.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue()) // descending sort
                .limit(topN)                                        // take top N
                .collect(Collectors.toList());                      // return as list
    }

    public void printReport() {
        System.out.println("\n=== ANALYTICS REPORT ===");
        System.out.println("Total orders: " + getTotalOrders());

        System.out.println("Total profit: " + Utils.formatPrice(getTotalProfit()));

        System.out.println("Top 3 best-selling products:");
        List<Map.Entry<Product, Integer>> topProducts = getTopBestSellingProducts(3);
        for (int i = 0; i < topProducts.size(); i++) {
            Map.Entry<Product, Integer> entry = topProducts.get(i);
            System.out.println((i + 1) + ". " + entry.getKey().getProductName() +
                    " - Sold: " + entry.getValue() + " units");
        }
    }
}
