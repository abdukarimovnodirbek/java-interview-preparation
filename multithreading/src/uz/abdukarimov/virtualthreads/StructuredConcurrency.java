package uz.abdukarimov.virtualthreads;

import java.util.concurrent.ExecutionException;

public class StructuredConcurrency {
/*    static void main() {
        // ── Muammo: "unstructured" concurrency ────────────────────────
        // Eski usul — tasklar o'rtasida munosabat yo'q
        Future<User> userFuture = executor.submit(() -> fetchUser(id));
        Future<Orders> ordersFuture = executor.submit(() -> fetchOrders(id));

        User user = userFuture.get();   // user xato bersa — orders bekor bo'lmaydi
        Orders orders = ordersFuture.get();
    }

    public record UserDashboard(User user, List<Order> orders,
                                double balance) {
    }

    public UserDashboard buildDashboard(String userId) throws InterruptedException, ExecutionException {
        // ── Structured Concurrency — StructuredTaskScope ───────────────
        // Java 21: --enable-preview kerak
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            // Subtasklar parallel ishga tushadi
            StructuredTaskScope.Subtask<User> userTask = scope.fork(() -> fetchUser(userId));

            StructuredTaskScope.Subtask<List<Order>> ordersTask = scope.fork(() -> fetchOrders(userId));

            StructuredTaskScope.Subtask<Double> balanceTask = scope.fork(() -> fetchBalance(userId));

            scope.join();           // barcha tugashini kutish
            scope.throwIfFailed();  // bitta xato bo'lsa exception otadi
            // va boshqa tasklar cancel bo'ladi!

            // Hammasi muvaffaqiyatli tugadi
            return new UserDashboard(
                    userTask.get(),
                    ordersTask.get(),
                    balanceTask.get()
            );

        } // scope yopilganda — barcha tugallanmagan tasklar cancel
    }

// ── ShutdownOnSuccess — birinchisi yetarli ────────────────────
// anyOf() ga o'xshash — birinchi muvaffaqiyatli javob kerak

    public String fetchFastest(String key)
            throws InterruptedException, ExecutionException {

        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {

            scope.fork(() -> fetchFromCache(key));   // tez
            scope.fork(() -> fetchFromDatabase(key)); // sekin
            scope.fork(() -> fetchFromNetwork(key));  // eng sekin

            scope.join();
            return scope.result(); // birinchi muvaffaqiyatli natija
            // Qolgan ikki task avtomatik cancel!
        }
    }*/
}
