package thread;

import lombok.RequiredArgsConstructor;
import model.Order;
import service.OrderGenerator;
import util.Utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


@RequiredArgsConstructor
public class CustomerTask implements Runnable {

    private static final AtomicInteger ORDER_ID_GENERATOR = new AtomicInteger(1);
    private final int customerId;
    private final BlockingQueue<Order> orderQueue;
    private final OrderGenerator orderGenerator;
    private final int numberOfOrders;

    @Override
    public void run() {
        for (int i = 0; i < numberOfOrders; i++) {
            int orderId = ORDER_ID_GENERATOR.getAndIncrement();
            Order order = orderGenerator.generateOrder(customerId, orderId);

            try {
                orderQueue.put(order); // add order to shared queue
                System.out.println("Customer " + customerId + " placed Order " + orderId);

                Utils.randomSleep(500);

            } catch (InterruptedException e) {
                System.err.println("Customer " + customerId + " interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Customer " + customerId + " finished placing orders.");
    }
}
