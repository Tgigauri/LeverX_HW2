package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int orderID;
    private int customerID;
    private Map<Product, Integer> products;
    private double totalPrice;
    private LocalDateTime timestamp = LocalDateTime.now();

}
