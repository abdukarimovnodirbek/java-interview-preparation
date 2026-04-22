package uz.abdukarimov.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreeWaysDemo {
    // ── Umumiy natija ────────────────────────────────────────────
    static List<String> log = Collections.synchronizedList(new ArrayList<>());

    // ══════════════════════════════════════════════════════════════
    // USUL 1 — Thread extend
    // ══════════════════════════════════════════════════════════════
    static class DownloadThread extends Thread {
        private final String url;

        DownloadThread(String url) {
            super("Downloader-" + url);
            this.url = url;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(600);
                log.add("✅ [Thread] Yuklandi: " + url);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // ══════════════════════════════════════════════════════════════
    // USUL 2 — Runnable implement
    // ══════════════════════════════════════════════════════════════
    static class ParserRunnable implements Runnable {
        private final String data;

        ParserRunnable(String data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(400);
                log.add("✅ [Runnable] Tahlil qilindi: " + data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // ══════════════════════════════════════════════════════════════
    // USUL 3 — Callable + Future
    // ══════════════════════════════════════════════════════════════
    static class AnalyzerCallable implements Callable<Integer> {
        private final String text;

        AnalyzerCallable(String text) {
            this.text = text;
        }

        @Override
        public Integer call() throws Exception {
            Thread.sleep(500);
            int wordCount = text.split("\\s+").length;
            log.add("✅ [Callable] So'z soni: " + wordCount + " — " + text);
            return wordCount;
        }
    }

    // ══════════════════════════════════════════════════════════════
    // MAIN
    // ══════════════════════════════════════════════════════════════
    static void main() throws Exception {

        long start = System.currentTimeMillis();

        // 1. Thread extend
        DownloadThread dt1 = new DownloadThread("file1.zip");
        DownloadThread dt2 = new DownloadThread("file2.zip");
        dt1.start();
        dt2.start();

        // 2. Runnable
        Thread rt1 = new Thread(new ParserRunnable("JSON data"));
        Thread rt2 = new Thread(() -> {  // lambda Runnable
            try {
                Thread.sleep(300);
                log.add("✅ [Lambda] Konvertatsiya qilindi");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        rt1.start();
        rt2.start();

        // 3. Callable + Future
        ExecutorService exec = Executors.newFixedThreadPool(2);
        Future<Integer> f1 = exec.submit(new AnalyzerCallable("Java is awesome"));
        Future<Integer> f2 = exec.submit(new AnalyzerCallable("Concurrency in Java"));

        // Barcha threadlar tugashini kutish
        dt1.join();
        dt2.join();
        rt1.join();
        rt2.join();

        int total = f1.get() + f2.get();
        log.add("📊 Jami so'zlar: " + total);

        exec.shutdown();

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("=== NATIJALAR (" + elapsed + "ms) ===");
        log.forEach(System.out::println);
    }
}

// Chiqish (~600ms — parallel ishladi!):
// === NATIJALAR (623ms) ===
// ✅ [Lambda]   Konvertatsiya qilindi
// ✅ [Runnable] Tahlil qilindi: JSON data
// ✅ [Callable] So'z soni: 3 — Java is awesome
// ✅ [Callable] So'z soni: 4 — Concurrency in Java
// ✅ [Thread]   Yuklandi: file1.zip
// ✅ [Thread]   Yuklandi: file2.zip
// 📊 Jami so'zlar: 7

//    ┌─────────────────┬────────────┬────────────┬───────────────────┐
//    │                 │   Thread   │  Runnable  │ Callable+Future   │
//    ├─────────────────┼────────────┼────────────┼───────────────────┤
//    │ Natija qaytaradi│     ❌     │     ❌     │        ✅         │
//    │ Exception otadi │     ❌     │     ❌     │        ✅         │
//    │ Meros olish     │  Thread    │  Ixtiyoriy │    Ixtiyoriy      │
//    │ Lambda bilan    │     ❌     │     ✅     │        ✅         │
//    │ ExecutorService │     ❌     │     ✅     │        ✅         │
//    │ Qachon ishlatish│ Kam (eski) │  Ko'p ishlatiladi  │ Natija kerak │
//    └─────────────────┴────────────┴────────────┴───────────────────┘
//
//    Thread Lifecycle:
//    NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → RUNNABLE → TERMINATED