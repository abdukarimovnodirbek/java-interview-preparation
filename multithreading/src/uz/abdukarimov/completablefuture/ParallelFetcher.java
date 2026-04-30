package uz.abdukarimov.completablefuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class ParallelFetcher {

    private final ExecutorService pool = Executors.newFixedThreadPool(6);

    // ── allOf — hammasi kerak ────────────────────────────────
    public record Dashboard(String user, List<String> orders,
                            double balance, String weather) {
    }

    public CompletableFuture<Dashboard> loadDashboard(String userId) {

        CompletableFuture<String> userCF =
                supplyAsync(() -> fetchUser(userId), pool);
        CompletableFuture<List<String>> ordersCF =
                supplyAsync(() -> fetchOrders(userId), pool);
        CompletableFuture<Double> balanceCF =
                supplyAsync(() -> fetchBalance(userId), pool);
        CompletableFuture<String> weatherCF =
                supplyAsync(() -> fetchWeather(), pool);

        return CompletableFuture
                .allOf(userCF, ordersCF, balanceCF, weatherCF)
                .thenApply(v -> new Dashboard(
                        userCF.join(),
                        ordersCF.join(),
                        balanceCF.join(),
                        weatherCF.join()
                ));
        // Parallel ishlaydi: jami vaqt = eng sekin task vaqti
        // (serial emas: hammasi qo'shilmaydi)
    }

    // ── anyOf — birinchi javob kerak (cache vs DB) ───────────
    public CompletableFuture<String> fetchFastest(String key) {

        CompletableFuture<String> fromCache =
                supplyAsync(() -> {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return cacheGet(key); // tez
                }, pool);

        CompletableFuture<String> fromDB =
                supplyAsync(() -> {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return dbGet(key); // sekin
                }, pool);

        return CompletableFuture
                .anyOf(fromCache, fromDB)
                .thenApply(result -> (String) result);
        // Cache 50ms da qaytsa — DB ni kutmaymiz
    }

    // ── allOf + xato boshqarish ──────────────────────────────
    public CompletableFuture<List<String>> fetchAllSafe(
            List<String> urls) {

        List<CompletableFuture<String>> futures = urls.stream()
                .map(url -> supplyAsync(() -> fetch(url), pool)
                        .exceptionally(ex -> "ERROR: " + ex.getMessage()))
                .collect(Collectors.toList());

        return CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    // Stub metodlar
    private String fetchUser(String id) {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        return "User-" + id;
    }

    private List<String> fetchOrders(String id) {
        try {
            Thread.sleep(150);
        } catch (Exception e) {
        }
        return List.of("Order-1", "Order-2");
    }

    private double fetchBalance(String id) {
        try {
            Thread.sleep(80);
        } catch (Exception e) {
        }
        return 1_500_000.0;
    }

    private String fetchWeather() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
        return "Toshkent: 28°C, Quyoshli";
    }

    private String cacheGet(String k) {
        return "cached:" + k;
    }

    private String dbGet(String k) {
        return "db:" + k;
    }

    private String fetch(String url) {
        return "data from " + url;
    }
}