package uz.abdukarimov.completablefuture;

import java.util.concurrent.CompletableFuture;

public class ThenApplyThenCompose {
    static void main() {
        // thenApply — oddiy transform (T → R)
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> "hello")
                        .thenApply(s -> s.length()); // String → Integer

        // thenCompose — CF qaytaruvchi funksiya (T → CF<R>)
        CompletableFuture<User> cf2 = CompletableFuture.supplyAsync(() -> "user-42")
                        .thenCompose(id -> findUserById(id)); // String → CF<User>

        // thenApply bilan nesting muammosi:
        CompletableFuture<CompletableFuture<User>> WRONG = CompletableFuture.supplyAsync(() -> "user-42")
                        .thenApply(id -> findUserById(id)); // CF<CF<User>> ❌

        // thenCompose yassilaydi:
        CompletableFuture<User> RIGHT = CompletableFuture.supplyAsync(() -> "user-42")
                        .thenCompose(id -> findUserById(id)); // CF<User> ✅
    }

    private static CompletableFuture<User> findUserById(String id) {
        return null;
    }

    record User(String id, String name) {
    }
}
