package uz.abdukarimov.completablefuture;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// Model klasslar
record User(String id, String name, String email) {
}

record Order(String id, String userId, double amount, String status) {
}

record Product(String id, String name, double price) {
}

record Invoice(User user, List<Order> orders,
               List<Product> products, double total) {
}

// Async REST Service
public class AsyncRestService {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .executor(Executors.newFixedThreadPool(8))
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    //private final ObjectMapper mapper = new ObjectMapper();

    // ── Asosiy GET so'rov ────────────────────────────────────
    private <T> CompletableFuture<T> getAsync(
            String url, Class<T> type) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        return httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw new RuntimeException(
                                "HTTP " + response.statusCode() + ": " + url);
                    }
                    return response.body();
                })
                .thenApply(body -> {
                    try {
                        return null; //mapper.readValue(body, type);
                    } catch (Exception e) {
                        throw new RuntimeException("Parse xato: " + e);
                    }
                });
    }

    // ── Foydalanuvchi olish ──────────────────────────────────
    public CompletableFuture<User> fetchUser(String userId) {
        return getAsync(
                "https://api.example.com/users/" + userId,
                User.class
        ).exceptionally(ex -> {
            System.err.println("User fetch xato: " + ex.getMessage());
            return new User(userId, "Unknown", "unknown@example.com");
        });
    }

    // ── Buyurtmalar olish ────────────────────────────────────
    public CompletableFuture<List<Order>> fetchOrders(String userId) {
        return getAsync(
                "https://api.example.com/users/" + userId + "/orders", null
                //new TypeReference<List<Order>>() {
                //}
        );/*.exceptionally(ex -> {
            System.err.println("Orders fetch xato: " + ex.getMessage());
            return null; //Collections.emptyList();
        });*/
    }

    // ── Mahsulot olish ───────────────────────────────────────
    public CompletableFuture<Product> fetchProduct(String productId) {
        return getAsync(
                "https://api.example.com/products/" + productId,
                Product.class
        );
    }

    // ── To'liq Hisob-faktura Pipeline ────────────────────────
    public CompletableFuture<Invoice> buildInvoice(String userId) {

        // 1. User va Orders ni PARALLEL olish
        CompletableFuture<User> userCF = fetchUser(userId);
        CompletableFuture<List<Order>> ordersCF = fetchOrders(userId);

        return userCF.thenCombine(ordersCF, (user, orders) -> {

            // 2. Har buyurtma uchun mahsulotlarni PARALLEL olish
            List<CompletableFuture<Product>> productFutures = orders
                    .stream()
                    .map(order -> fetchProduct(order.id()))
                    .collect(Collectors.toList());

            // 3. Barcha mahsulotlar tugashini kutish
            CompletableFuture<List<Product>> allProducts =
                    CompletableFuture
                            .allOf(productFutures.toArray(new CompletableFuture[0]))
                            .thenApply(v -> productFutures.stream()
                                    .map(CompletableFuture::join)
                                    .collect(Collectors.toList()));

            // 4. Hisob-faktura qurish
            return allProducts.thenApply(products -> {
                double total = orders.stream()
                        .mapToDouble(Order::amount).sum();
                return new Invoice(user, orders, products, total);
            });

        }).thenCompose(cf -> cf); // CF<CF<Invoice>> → CF<Invoice>
    }

    // ── Retry mexanizmi ──────────────────────────────────────
    public <T> CompletableFuture<T> withRetry(
            Supplier<CompletableFuture<T>> action,
            int maxRetries) {

        CompletableFuture<T> result = action.get();

        for (int i = 0; i < maxRetries; i++) {
            final int attempt = i + 1;
            result = result.exceptionallyCompose(ex -> {
                System.out.printf("Urinish %d muvaffaqiyatsiz: %s%n",
                        attempt, ex.getMessage());
                // 1s kutib qayta urinish
                return null; //CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(action.get());
            });
        }
        return result;
    }

    // ── Timeout qo'shish ─────────────────────────────────────
    public <T> CompletableFuture<T> withTimeout(
            CompletableFuture<T> cf,
            long timeout,
            TimeUnit unit,
            T fallback) {

        return cf.completeOnTimeout(fallback, timeout, unit)
                .exceptionally(ex -> fallback);
        // Java 9+: orTimeout() ham bor (exception otadi)
    }

    // ── Main — ishlatish ─────────────────────────────────────
    static void main() {
        AsyncRestService service = new AsyncRestService();

        long start = System.currentTimeMillis();

        // To'liq pipeline
        service.buildInvoice("user-42")
                .thenApply(invoice -> {
                    System.out.printf("Foydalanuvchi: %s%n",
                            invoice.user().name());
                    System.out.printf("Buyurtmalar: %d ta%n",
                            invoice.orders().size());
                    System.out.printf("Jami: %.2f so'm%n",
                            invoice.total());
                    return invoice;
                })
                .exceptionally(ex -> {
                    System.err.println("Pipeline xato: " + ex.getMessage());
                    return null;
                })
                .whenComplete((inv, ex) -> {
                    long elapsed = System.currentTimeMillis() - start;
                    System.out.printf("Vaqt: %dms%n", elapsed);
                })
                .join(); // main thread kutadi

        // Retry bilan
        service.withRetry(() -> service.fetchUser("user-99"), 3)
                .thenAccept(user -> System.out.println("User: " + user.name()))
                .join();
    }
}