package uz.abdukarimov.virtualthreads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class VirtualThreadDemo {
    static void main() throws InterruptedException {
        // ── 1. Thread.ofVirtual() — to'g'ridan ────────────────────────
        Thread vt = Thread.ofVirtual()
                .name("my-virtual-thread")
                .start(() -> System.out.println("Virtual thread ishlayapti!"));

        vt.join();

// ── 2. Thread.startVirtualThread() — eng qisqa ────────────────
        Thread vt2 = Thread.startVirtualThread(() -> {
            System.out.println("Thread: " + Thread.currentThread());
            System.out.println("Virtual: " + Thread.currentThread().isVirtual());
        });
        vt2.join();

// ── 3. ExecutorService bilan (tavsiya etiladi) ─────────────────
// Har submit() da yangi virtual thread yaratadi — pool EMAS!
        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<String>> futures = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                final int id = i;
                futures.add(executor.submit(() -> {
                    Thread.sleep(100); // I/O simulate
                    return "Task-" + id + " [" +
                            Thread.currentThread().isVirtual() + "]";
                }));
            }

            for (Future<String> f : futures) {
                System.out.println(f.get());
            }
        } // try-with-resources: shutdown avtomatik
        catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

// ── 4. ThreadFactory bilan ─────────────────────────────────────
        ThreadFactory factory = Thread.ofVirtual()
                .name("vt-", 0)          // vt-0, vt-1, vt-2 ...
                .factory();

        ExecutorService pool = Executors.newThreadPerTaskExecutor(factory);
    }
}
