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
    private final List<Order> processedOrders;


    public long getTotalOrders() {
        return processedOrders.size(); // 
    }


    public double getTotalProfit() {
        return processedOrders.stream()
                .mapToDouble(Order::getTotalPrice) 
                .sum();                             
    }


    public List<Map.Entry<Product, Integer>> getTopBestSellingProducts(int topN) {

        Map<Product, Integer> productSales = new HashMap<>();

        for (Order order : processedOrders) {
            for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                productSales.merge(product, quantity, Integer::sum);
            }
        }

        return productSales.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue()) 
                .limit(topN)                                        
                .collect(Collectors.toList());                      
    }

    public void printReport() {
        System.out.println("ANALYTICS:");
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
