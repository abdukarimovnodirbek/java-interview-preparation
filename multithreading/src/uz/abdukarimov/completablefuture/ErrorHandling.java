package uz.abdukarimov.completablefuture;

import java.util.concurrent.CompletableFuture;

public class ErrorHandling {
    static void main() {
        CompletableFuture<String> pipeline =
                CompletableFuture.supplyAsync(() -> {
                            // ishi...
                            throw new RuntimeException("Server xato");
                        })

                        // ── exceptionally — faqat xatoda ────────────────────────
                        .exceptionally(ex -> {
                            System.out.println("exceptionally: " + ex.getCause().getMessage());
                            return "fallback qiymat"; // zanjir davom etadi
                        })

                        // ── handle — har doim (xato ham, normal ham) ─────────────
                        .handle((result, ex) -> {
                            if (ex != null) {
                                return "handle fallback: " + ex.getMessage();
                            }
                            return result + " [handle orqali o'tdi]";
                        })

                        // ── whenComplete — faqat kuzatish (o'zgartirmaydi) ───────
                        .whenComplete((result, ex) -> {
                            if (ex != null) {
                                System.out.println("Xato loglandi: " + ex);
                            } else {
                                System.out.println("Natija loglandi: " + result);
                            }
                            // bu qaytargan qiymat e'tiborga olinmaydi!
                        });

        System.out.println(pipeline.join());
    }
}

//        supplyAsync → xato → thenApply (o'tkaziladi) → exceptionally (tutadi) → thenApply (ishlaydi)
//
//        exceptionally bo'lmasa:
//        supplyAsync → xato → thenApply (o'tkaziladi) → thenApply (o'tkaziladi) → .join() da exception