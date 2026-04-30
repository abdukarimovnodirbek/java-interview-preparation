package uz.abdukarimov.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static void main() {
        //    // Future — bloklaydi (yomon)
        //    Future<String> f = executor.submit(() -> fetchData());
        //    String result = f.get(); // shu yerda thread to'xtaydi!

        //    // CompletableFuture — zanjir, bloklash yo'q
        //    CompletableFuture.supplyAsync(() -> fetchData())
        //            .thenApply(data -> parse(data))
        //            .thenAccept(System.out::println);
        //    // main thread davom etadi!


        // 1. supplyAsync — natija qaytaradi
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> "Hello");

        // 2. runAsync — natija qaytarmaydi (void)
        CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> System.out.println("Background ish"));

        // 3. Custom Executor bilan
        ExecutorService pool = Executors.newFixedThreadPool(4);

        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> heavyWork(), pool);

        // 4. Tayyor qiymat bilan (allaqachon tugagan)
        CompletableFuture<Integer> done = CompletableFuture.completedFuture(42);

        // 5. Qo'lda to'ldirish
        CompletableFuture<String> manual = new CompletableFuture<>();
        manual.complete("natija");           // muvaffaqiyatli
        manual.completeExceptionally(        // xato bilan
                new RuntimeException("xato"));
    }

    private static String heavyWork() {
        return null;
    }
}

/*
         CompletableFuture qoidalari:
         ─────────────────────────────────────────────────────────────
         supplyAsync   → asinxron boshlash
         thenApply     → transform  (T→R)
         thenCompose   → CF qaytaruvchi transform  (T→CF<R>)
         thenCombine   → ikki CF natijasini birlashtirish
         allOf         → hammasi tugashini kutish (parallel)
         anyOf         → birinchisi tugashini kutish
         exceptionally → xato bo'lsa fallback
         handle        → har doim (xato ham, normal ham)
         whenComplete  → faqat kuzatish/logging
         ─────────────────────────────────────────────────────────────
         join() faqat eng oxirida — bu yerda bloklanadi
         I/O tasklar → Async + custom pool
         CPU tasklar → thenApply (overhead kamaytiradi)
         ─────────────────────────────────────────────────────────────
*/

/*
        ┌─────────────────────────────────────┬────────────────────────────────┐
        │ Metod                               │ Vazifa                         │
        ├─────────────────────────────────────┼────────────────────────────────┤
        │ TRANSFORM (bir CF)                  │                                │
        │ thenApply(fn)                       │ T → R  (map)                   │
        │ thenCompose(fn)                     │ T → CF<R>  (flatMap)           │
        │ thenAccept(consumer)                │ T → void  (faqat iste'mol)     │
        │ thenRun(runnable)                   │ void → void  (natija yo'q)     │
        ├─────────────────────────────────────┼────────────────────────────────┤
        │ COMBINE (ikki CF)                   │                                │
        │ thenCombine(cf2, fn)                │ T + U → R                      │
        │ thenAcceptBoth(cf2, consumer)       │ T + U → void                   │
        │ runAfterBoth(cf2, runnable)         │ ikkalasi tugasa → run          │
        │ applyToEither(cf2, fn)              │ birinchisi → R                 │
        │ acceptEither(cf2, consumer)         │ birinchisi → void              │
        ├─────────────────────────────────────┼────────────────────────────────┤
        │ STATIC (ko'p CF)                    │                                │
        │ allOf(cf1, cf2, ...)                │ hammasi tugasa → CF<Void>      │
        │ anyOf(cf1, cf2, ...)                │ birinchisi tugasa → CF<Object> │
        ├─────────────────────────────────────┼────────────────────────────────┤
        │ XATO BOSHQARISH                     │                                │
        │ exceptionally(fn)                   │ faqat xatoda, fallback         │
        │ handle(bifn)                        │ har doim (result|ex)           │
        │ whenComplete(bifn)                  │ kuzatish, o'zgartirmaydi       │
        │ exceptionallyCompose(fn)            │ xatoda CF qaytarish (retry)    │
        ├─────────────────────────────────────┼────────────────────────────────┤
        │ BLOCKING (zarur bo'lganda)          │                                │
        │ get()                               │ checked exception              │
        │ join()                              │ unchecked exception            │
        │ getNow(default)                     │ tugagan bo'lsa, aks default    │
        └─────────────────────────────────────┴────────────────────────────────┘
*/
