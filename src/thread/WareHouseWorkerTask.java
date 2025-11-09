package thread;

import lombok.AllArgsConstructor;
import model.Order;
import model.Product;
import service.Warehouse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
public class WareHouseWorkerTask implements Runnable {

    private final BlockingQueue<Order> orderQueue;
    private final Warehouse warehouse;
    private final List<Order> processedOrders;

    @Override
    public void run() {
        while (true) {
            try {
                // Take next order from queue (blocks if empty)
                Order order = orderQueue.take();

                // Process the order (deduct stock from warehouse)
                processOrder(order);

                // Add the order to processedOrders list
                processedOrders.add(order);

            } catch (InterruptedException e) {
                System.out.println("Warehouse worker interrupted, stopping.");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void processOrder(Order order) {
        for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            boolean success = warehouse.remove(product, quantity);
            if (!success) {
                System.out.println("Warehouse: Not enough stock for " + product.getProductName() +
                        " (Order " + order.getOrderID() + ")");
            }
        }
        System.out.println("Warehouse processed Order " + order.getOrderID());
    }
}

