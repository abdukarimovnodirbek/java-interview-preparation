package uz.abdukarimov.gc;

public class GCTypes {
// Java da mavjud GC lari:

// Serial GC      → -XX:+UseSerialGC
//   Bitta thread, kichik ilovalar uchun
//   Embedded, CLI dasturlar

// Parallel GC    → -XX:+UseParallelGC  (Java 8 default)
//   Ko'p thread, batch processing
//   Throughput muhim bo'lganda

// G1 GC          → -XX:+UseG1GC       (Java 9+ default)
//   Heap ni regionlarga bo'ladi
//   Katta heap (4GB+), Low-latency

// ZGC            → -XX:+UseZGC        (Java 15+ production ready)
//   Sub-millisecond pause (<1ms)
//   Juda katta heap (terabyte ga qadar)
//   Java 21+ da default bo'lishi mumkin

// Shenandoah     → -XX:+UseShenandoahGC (Red Hat)
//   ZGC ga o'xshash, concurrent GC
//   OpenJDK da bor, Oracle JDK da yo'q

// Qaysi GC tanlash?
// Kichik ilova, RAM kam      → SerialGC
// Throughput muhim (batch)   → ParallelGC
// Response time muhim        → G1GC
// Ultra-low latency (fintech) → ZGC
}

/*
JVM Xotira bo'limlari:
        ──────────────────────────────────────────────────────
Heap        │ -Xms (boshlang'ich)  -Xmx (maksimal)
        Young     │   Eden + Survivor0 + Survivor1
        Old Gen   │   uzoq yashagan obyektlar
        ──────────────────────────────────────────────────────
        Stack       │ har thread uchun alohida   -Xss
        Metaspace   │ klass ta'riflari           -XX:MaxMetaspaceSize
        Code Cache  │ JIT natijasi              -XX:ReservedCodeCacheSize
        ──────────────────────────────────────────────────────
        ClassLoader ierarxiyasi:
        Bootstrap → Platform → Application → Custom
        Delegation: avval otaga, keyin o'zi
                            ──────────────────────────────────────────────────────
                            GC turlari:
                            Serial    → kichik ilova
                            Parallel  → throughput (Java 8 default)
G1        → balanced (Java 9+ default)
ZGC       → ultra-low latency
──────────────────────────────────────────────────────
Muammo va yechim:
heap space    → -Xmx oshir, memory leak top
Metaspace     → ClassLoader leak tekshir
StackOverflow → -Xss oshir, rekursiya chukurligini kamayt
*/