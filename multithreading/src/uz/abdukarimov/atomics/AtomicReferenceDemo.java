package uz.abdukarimov.atomics;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {
    static void main() {
        // ── AtomicReference<T> — har qanday object ────────────────────
        AtomicReference<String> ref = new AtomicReference<>("initial");

        ref.get();                        // "initial"
        ref.set("updated");
        ref.getAndSet("new");             // "updated" qaytaradi
        ref.compareAndSet("new", "final"); // true

// Immutable object yangilash — thread-safe pattern
        record Config(String host, int port) {
        }

        AtomicReference<Config> configRef =
                new AtomicReference<>(new Config("localhost", 8080));

// CAS bilan yangilash
        Config oldCfg, newCfg;
        do {
            oldCfg = configRef.get();
            newCfg = new Config(oldCfg.host(), oldCfg.port() + 1);
        } while (!configRef.compareAndSet(oldCfg, newCfg));

// ── AtomicLong — katta sonlar ─────────────────────────────────
        AtomicLong totalBytes = new AtomicLong(0);
        totalBytes.addAndGet(1024L * 1024);  // 1MB qo'shish

// ── AtomicBoolean — flag ──────────────────────────────────────
        AtomicBoolean started = new AtomicBoolean(false);

// Faqat bir marta bajarilsin:
        if (started.compareAndSet(false, true)) {
            System.out.println("Faqat bir marta ishlaydi!");
        }
    }
}
