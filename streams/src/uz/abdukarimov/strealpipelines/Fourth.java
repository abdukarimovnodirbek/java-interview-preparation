package uz.abdukarimov.strealpipelines;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Fourth {
    static void main() {
        record Order(String product, String category, int qty, double price) {
            double total() {
                return qty * price;
            }
        }

        List<Order> orders = List.of(
                new Order("Laptop", "Electronics", 2, 1200.0),
                new Order("Phone", "Electronics", 5, 800.0),
                new Order("Desk", "Furniture", 3, 300.0),
                new Order("Chair", "Furniture", 6, 150.0),
                new Order("Monitor", "Electronics", 4, 400.0),
                new Order("Lamp", "Furniture", 8, 50.0)
        );

        // Kategoriya bo'yicha umumiy savdo
        Map<String, Double> salesByCategory = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::category,
                        Collectors.summingDouble(Order::total)
                ));
        // {Electronics=7200.0, Furniture=2200.0}

        // Eng yuqori daromadli mahsulot
        orders.stream()
                .max(Comparator.comparingDouble(Order::total))
                .ifPresent(o -> System.out.println(
                        "Top: " + o.product() + " = $" + o.total()
                ));
        // Top: Laptop = $2400.0

        // $1000 dan yuqori buyurtmalar soni
        long bigOrders = orders.stream()
                .filter(o -> o.total() > 1000)
                .count();
        // 2
    }
}
