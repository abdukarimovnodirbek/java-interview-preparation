package uz.abdukarimov.completablefuture;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;
import static java.rmi.server.LogStream.log;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class OperatorsChain {
    static void main() throws ExecutionException, InterruptedException {
        // supplyAsync — asinxron boshlash
        // supplyAsync(Supplier) → CF Transform
        // Yangi thread da ish boshlaydi. main thread bloklanmaydi. Default: ForkJoinPool.commonPool().

        CompletableFuture cf =
                supplyAsync(() -> {
                    try {
                        sleep(500);   // og'ir ish
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "Ma'lumot";
                });

        System.out.println("Main davom etmoqda...");
        String result = cf.get().toString(); // faqat kerak bo'lganda kutadi

        // ------------------------------------------------------------------- //
        // thenApply — natijani o'zgartirish
        // thenApply(Function) → CF Transform
        // Oldingi natijani olib, yangi natija qaytaradi. map() ga o'xshash. Bir xil thread da ishlashi mumkin.

        CompletableFuture cf1 =
                supplyAsync(() -> "hello world")
                        .thenApply(String::toUpperCase)   // "HELLO WORLD"
                        .thenApply(String::length);       // 11

        // Zanjir:  "hello world" → "HELLO WORLD" → 11

        // ------------------------------------------------------------------- //
        // thenCompose — CF qaytaruvchi funksiya
        // thenCompose(Function>) → CF Transform
        // flatMap() ga o'xshash. Funksiya o'zi CF qaytarganda ishlatiladi. Nested CF dan qochish uchun.

        // thenApply bilan — noto'g'ri nesting
        //        CF> wrong = cf.thenApply(id -> fetchUser(id));

        // thenCompose bilan — to'g'ri yassilash
        // CF right = cf.thenCompose(id -> fetchUser(id));

        CompletableFuture result1 =
                supplyAsync(() -> "user-123")
                        .thenCompose(id -> fetchUser(id))      // CF
                        .thenCompose(user -> fetchOrders(user)); // CF

        // ------------------------------------------------------------------- //
        // thenCombine — ikki CF ni birlashtirish
        // thenCombine(CF, BiFunction) → CF Combine
        // Ikki mustaqil CF parallel ishlaydi. Ikkalasi tugagach BiFunction chaqiriladi.

        CompletableFuture userCF = supplyAsync(() -> fetchUser("user-123"));
        CompletableFuture ordersCF = supplyAsync(() -> fetchOrders("user-123"));

        // Parallel ishlaydi, ikkalasi tugagach birlashtiradi
        CompletableFuture result2 = userCF.thenCombine(ordersCF, (user, orders) ->
                user + " has " + orders + " orders");

        // ------------------------------------------------------------------- //
        // allOf — hammasi tugashini kutish
        // allOf(CF...) → CF Combine
        // Barcha CF parallel ishlaydi. Hammasi tugaganda davom etadi. Bitta xato bo'lsa — barchasi fail.

        CompletableFuture cf11 = supplyAsync(() -> fetchUser(""));
        CompletableFuture cf2 = supplyAsync(() -> fetchOrders(""));
        CompletableFuture cf3 = supplyAsync(() -> fetchBalance());

        CompletableFuture.allOf(cf11, cf2, cf3)
                .thenRun(() -> {
                    String user = cf1.join().toString();  // allaqachon tugagan
                    List orders = Collections.singletonList(cf2.join());
                    Double bal = (Double) cf3.join();
                    System.out.println("Hammasi tayyor!");
                });
        // ------------------------------------------------------------------- //
        // anyOf — birinchisi tugashini kutish
        // anyOf(CF...) → CF Combine
        // Birinchi tugagan CF natijasini qaytaradi. Boshqalari bekor qilinmaydi — ular ham ishlashda davom etadi.

        CompletableFuture fast = supplyAsync(() -> {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "tez";
        });
        CompletableFuture medium = supplyAsync(() -> {
            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "o'rta";
        });
        CompletableFuture slow = supplyAsync(() -> {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "sekin";
        });

        CompletableFuture.anyOf(fast, medium, slow)
                .thenAccept(result3 ->
                        System.out.println("Birinchi: " + result3)
                );
        // Chiqish: Birinchi: tez
        // ------------------------------------------------------------------- //
        // exceptionally — xatoni tuzatish
        // exceptionally(Function) → CF Transform
        // Zanjirda xato bo'lsa fallback qiymat qaytaradi. Xato bo'lmasa — bu blok o'tkazib yuboriladi.

        CompletableFuture cf4 =
                CompletableFuture.supplyAsync(() -> {
                            if (Math.random() > 0.5) {
                                throw new RuntimeException("Server xato!");
                            }
                            return "Ma'lumot";
                        })
                        .exceptionally(ex -> {
                            System.out.println("Xato: " + ex.getMessage());
                            return "Default ma'lumot";   // fallback
                        });
        // ------------------------------------------------------------------- //
        // handle — har doim chaqiriladi
        // handle(BiFunction) → CF Transform
        // Muvaffaqiyatli ham, xatoli ham holatda chaqiriladi. Biri null bo'ladi. exceptionally + thenApply birlashmasi.

        CompletableFuture cf6 =
                CompletableFuture.supplyAsync(() -> fetchUser(""))
                        .handle((result3, ex) -> {
                            if (ex != null) {
                                log("Xato: " + ex.getMessage());
                                return "default"; // xato holat
                            }
                            return result3; // normal holat
                        });

        // whenComplete — o'zgartirmaydi, faqat kuzatadi
        cf.whenComplete((res, ex) -> {
            //if (ex != null) metrics.recordError();
            //else metrics.recordSuccess();
        });
    }

    /*
        // thenApply    — oldingi thread (yoki main) da
        // thenApplyAsync — ForkJoinPool da (yangi thread)
        // thenApplyAsync(fn, executor) — custom pool da

        CompletableFuture<String> cf =
                CompletableFuture.supplyAsync(() -> fetchData())     // pool-1
                        .thenApply(s -> s.toUpperCase())                 // pool-1
                        .thenApplyAsync(s -> process(s))                 // ForkJoin
                        .thenApplyAsync(s -> save(s), customPool);       // customPool

        // Qoida: I/O bo'lsa → Async + custom I/O pool
        //        CPU bo'lsa → thenApply (yangi thread kerак emas)
    */
    private static Object fetchBalance() {
        return null;
    }

    private static CompletionStage<Object> fetchOrders(Object user) {
        return null;
    }

    private static CompletionStage<Object> fetchUser(String id) {
        return null;
    }
}
