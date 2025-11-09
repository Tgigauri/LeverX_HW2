import model.Order;
import model.Product;
import service.Analytics;
import service.OrderGenerator;
import service.Warehouse;
import thread.CustomerTask;
import thread.WareHouseWorkerTask;
import util.DataSeeder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        // 1️⃣ Seed product catalog
        List<Product> catalog = DataSeeder.seedProducts();

        // 2️⃣ Initialize warehouse and add initial stock
        Warehouse warehouse = new Warehouse();
        DataSeeder.seedWarehouse(warehouse, catalog, 50); // 50 units per product

        // 3️⃣ Shared queue for orders (producer-consumer pattern)
        BlockingQueue<Order> ordersQueue = new LinkedBlockingQueue<>();

        // 4️⃣ Shared list to track processed orders (thread-safe)
        List<Order> processedOrders = Collections.synchronizedList(new ArrayList<>());

        // 5️⃣ Initialize OrderGenerator
        OrderGenerator orderGenerator = new OrderGenerator(catalog);

        // 6️⃣ Start customer threads (producers)
        int numberOfCustomers = 3;     // 3 customers
        int ordersPerCustomer = 5;     // each customer places 5 orders
        ExecutorService customerExecutor = Executors.newFixedThreadPool(numberOfCustomers);

        for (int customerId = 1; customerId <= numberOfCustomers; customerId++) {
            customerExecutor.submit(new CustomerTask(customerId, ordersQueue, orderGenerator, ordersPerCustomer));
        }

        // 7️⃣ Start warehouse worker threads (consumers)
        int numberOfWorkers = 2;  // number of warehouse workers
        ExecutorService workerExecutor = Executors.newFixedThreadPool(numberOfWorkers);

        for (int i = 0; i < numberOfWorkers; i++) {
            workerExecutor.submit(new WareHouseWorkerTask(ordersQueue, warehouse, processedOrders));
        }

        // 8️⃣ Wait for customers to finish placing orders
        customerExecutor.shutdown();
        customerExecutor.awaitTermination(5, TimeUnit.MINUTES); // wait until all customers finish

        // 9️⃣ Give warehouse some time to process remaining orders
        while (!ordersQueue.isEmpty()) {
            Thread.sleep(500); // wait 0.5s and check again
        }

        // 1️⃣0️⃣ Stop warehouse workers
        workerExecutor.shutdownNow(); // interrupts the infinite loop
        workerExecutor.awaitTermination(1, TimeUnit.MINUTES);

        // 1️⃣1️⃣ Run analytics
        Analytics analytics = new Analytics(processedOrders);
        analytics.printReport();
    }
}