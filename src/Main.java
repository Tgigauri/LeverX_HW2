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

        List<Product> catalog = DataSeeder.seedProducts();

        Warehouse warehouse = new Warehouse();
        DataSeeder.seedWarehouse(warehouse, catalog, 50); 

        BlockingQueue<Order> ordersQueue = new LinkedBlockingQueue<>();

        List<Order> processedOrders = Collections.synchronizedList(new ArrayList<>());

        OrderGenerator orderGenerator = new OrderGenerator(catalog);

        int numberOfCustomers = 3;   
        int ordersPerCustomer = 5;    
        ExecutorService customerExecutor = Executors.newFixedThreadPool(numberOfCustomers);

        for (int customerId = 1; customerId <= numberOfCustomers; customerId++) {
            customerExecutor.submit(new CustomerTask(customerId, ordersQueue, orderGenerator, ordersPerCustomer));
        }

        int numberOfWorkers = 2; 
        ExecutorService workerExecutor = Executors.newFixedThreadPool(numberOfWorkers);

        for (int i = 0; i < numberOfWorkers; i++) {
            workerExecutor.submit(new WareHouseWorkerTask(ordersQueue, warehouse, processedOrders));
        }

        customerExecutor.shutdown();
        customerExecutor.awaitTermination(5, TimeUnit.MINUTES);

        while (!ordersQueue.isEmpty()) {
            Thread.sleep(500); 
        }

        workerExecutor.shutdownNow(); 
        workerExecutor.awaitTermination(1, TimeUnit.MINUTES);

        Analytics analytics = new Analytics(processedOrders);
        analytics.printReport();
    }
}