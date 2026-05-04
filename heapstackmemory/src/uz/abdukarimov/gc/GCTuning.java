package uz.abdukarimov.gc;

public class GCTuning {
// ── Bosqich 1: GC log yig'ish ─────────────────────────────────
/*
    java -Xms2g -Xmx4g \
            -XX:+UseG1GC \
            -Xlog:gc*:file=gc.log:time,uptime \
            -XX:+HeapDumpOnOutOfMemoryError \
            -jar app.jar
*/

// ── Bosqich 2: Asosiy ko'rsatkichlarni o'lchash ────────────────
// GCEasy.io yoki GCViewer bilan log tahlil:
// - Throughput: %GC emas vaqt (>95% bo'lsin)
// - Average pause: maqsadli SLA ga mos?
// - Full GC soni: 0 bo'lishi kerak!
// - Heap growth trend: o'sib borayotganmi? = memory leak

// ── Bosqich 3: GC turi tanlash ────────────────────────────────
// Latency SLA < 10ms   → ZGC yoki Shenandoah
// Latency SLA < 200ms  → G1GC
// Throughput muhim     → Parallel GC
// Tiny heap (<256MB)   → Serial GC

// ── Bosqich 4: Heap o'lchamini tanlash ────────────────────────
// Qoida: Live Set * 3 = Xmx (minimum)
// Live Set = to'liq GC dan so'ng heap ishlatilishi

// jmap bilan Live Set o'lchash:
// jmap -histo:live <pid> | head -30

// ── Bosqich 5: GC parametrlarini sozlash ──────────────────────

// G1 uchun:
// -XX:MaxGCPauseMillis=100     → agresiv, G1 Young Gen ni kichraytiradi
// -XX:G1HeapRegionSize=8m      → Humongous objectlar bo'lsa kattalash
// -XX:G1ReservePercent=10      → Emergency buffer (default 10%)
// -XX:ConcGCThreads=4          → concurrent GC thread soni

// ZGC uchun:
// -XX:+UseZGC
// -XX:+ZGenerational           → Java 21+ da tavsiya etiladi
// -XX:SoftMaxHeapSize=3g       → "soft" chegara, OS ga xotira qaytaradi
// -Xlog:gc*                    → ZGC log

// ── Production flaglar to'plami ───────────────────────────────
// Server ilova (G1, 8GB RAM):
// -Xms4g -Xmx4g
// -XX:+UseG1GC
// -XX:MaxGCPauseMillis=200
// -XX:G1HeapRegionSize=16m
// -XX:+HeapDumpOnOutOfMemoryError
// -XX:HeapDumpPath=/var/log/heap.hprof
// -Xlog:gc*:file=/var/log/gc.log:time,uptime:filecount=5,filesize=20m
}
