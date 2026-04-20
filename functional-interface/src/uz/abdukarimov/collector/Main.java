package uz.abdukarimov.collector;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {
    static void main() {
        // Statistics
        List<Double> scores = List.of(85.5, 92.0, 78.3, 95.5, 88.0, 72.5);

        StatisticsCollector.Stats stats = scores.stream().collect(new StatisticsCollector());

        System.out.println(stats);
        // Stats{count=6, sum=511.8, min=72.5, max=95.5, avg=85.3}

        //---------------
        // 1. Top-N Collector — eng katta N elementni yig'ish
        List<Integer> nums = List.of(3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
        List<Integer> top3 = nums.stream().collect(topN(3));
        System.out.println(top3); // [9, 6, 5]

        //---------------
        // 2. Joining with limit
        List<String> words = List.of("Java", "Stream", "API", "Lambda", "Optional");
        String result = words.stream()
                .collect(joiningWithLimit(", ", 3));
        System.out.println(result); // Java, Stream, API

        //---------------
        // 3. Frequency Map Collector
        List<String> letters = List.of("a", "b", "a", "c", "b", "a", "d");
        Map<String, Long> freq = letters.stream().collect(frequencyMap());
        System.out.println(freq); // {a=3, b=2, c=1, d=1}

        // Barchasini birlashtirgan real misol:
        record Product(String name, String category, double price, int stock) {
        }

        List<Product> products = List.of(
                new Product("Laptop", "Electronics", 1200, 15),
                new Product("Phone", "Electronics", 800, 30),
                new Product("Desk", "Furniture", 300, 10),
                new Product("Chair", "Furniture", 150, 25),
                new Product("Monitor", "Electronics", 400, 20),
                new Product("Lamp", "Furniture", 50, 8)
        );

        // Kategoriya bo'yicha: aktiv (stock>10) mahsulotlar,
        // eng qimmat 2 tasini formatlangan string sifatida
        Map<String, String> report = products.stream()
                .filter(p -> p.stock() > 10)                          // Predicate
                .sorted(Comparator.comparingDouble(Product::price)    // sorted
                        .reversed())
                .collect(Collectors.groupingBy(
                        Product::category,                                 // guruhlash
                        Collectors.mapping(
                                p -> p.name() + "($" + p.price() + ")",       // Function
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream()
                                                .limit(2)
                                                .collect(Collectors.joining(", "))
                                )
                        )
                ));

        report.forEach((cat, prods) ->
                System.out.println(cat + ": " + prods)
        );
        // Electronics: Laptop($1200.0), Phone($800.0)
        // Furniture: Chair($150.0)
    }

    // 1. Top-N Collector — eng katta N elementni yig'ish
    public static <T extends Comparable<T>> Collector<T, ?, List<T>> topN(int n) {
        return Collector.of(
                () -> new TreeSet<T>(Comparator.reverseOrder()), // supplier
                (set, item) -> {                                  // accumulator
                    set.add(item);
                    if (set.size() > n) set.pollLast();           // kichigini o'chir
                },
                (set1, set2) -> {
                    set1.addAll(set2);
                    return set1;
                }, // combiner
                set -> new ArrayList<>(set)                       // finisher
        );
    }

    // 2. Joining with limit
    public static Collector<String, ?, String> joiningWithLimit(String delimiter, int maxItems) {
        return Collector.of(
                (Supplier<ArrayList<String>>) ArrayList::new,
                (list, item) -> {
                    if (list.size() < maxItems) list.add(item);
                },
                (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                },
                list -> String.join(delimiter, list)
        );
    }

    // 3. Frequency Map Collector
    public static <T> Collector<T, ?, Map<T, Long>> frequencyMap() {
        return Collector.of(
                LinkedHashMap::new,
                (map, item) -> map.merge(item, 1L, Long::sum),
                (m1, m2) -> {
                    m2.forEach((k, v) -> m1.merge(k, v, Long::sum));
                    return m1;
                }
        );
    }
}
