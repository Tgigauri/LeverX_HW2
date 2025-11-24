package service;


import model.Product;

import java.util.concurrent.ConcurrentHashMap;

public class Warehouse {

    public final ConcurrentHashMap<Product, Integer> inventory = new ConcurrentHashMap<>();


    public void add(Product p) {
        inventory.merge(p, 1, Integer::sum);
    }

    public void add(Product p, int quantity) {
        inventory.merge(p, quantity, Integer::sum);
    }

    public boolean isAvailable(Product p, int quantity) {
        return inventory.getOrDefault(p, 0) >= quantity;
    }

    public boolean remove(Product product, int quantity) {
        return inventory.compute(product, (p, currentQty) -> {
            if (currentQty == null || currentQty < quantity) return currentQty;
            return currentQty - quantity;
        }) != null && inventory.get(product) != null && inventory.get(product) <= inventory.get(product) + quantity;
    }


}
