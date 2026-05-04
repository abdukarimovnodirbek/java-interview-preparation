package uz.abdukarimov.gc;

public class G1vsGC {
// G1 GC tuning:
// -XX:+UseG1GC
// -XX:MaxGCPauseMillis=200     → maqsadli pauza (SLA)
// -XX:G1HeapRegionSize=16m     → region hajmi (1-32MB, 2^N)
// -XX:G1NewSizePercent=20      → Young gen minimum %
// -XX:G1MaxNewSizePercent=60   → Young gen maximum %
// -XX:G1MixedGCCountTarget=8  → Mixed GC necha marta
// -XX:InitiatingHeapOccupancyPercent=45  → concurrent marking boshlash

// G1 GC bosqichlari:
// 1. Minor GC      — Young gen yig'ish (STW, parallel)
// 2. Concurrent Marking — tirik obyektlarni aniqlash (concurrent)
//    - Initial Mark (STW, qisqa)
//    - Root Scan (concurrent)
//    - Concurrent Mark
//    - Remark (STW, qisqa)
//    - Cleanup (STW, qisqa) + concurrent
// 3. Mixed GC      — Young + ba'zi Old regionlar (STW)
// 4. Full GC       — faqat eng og'ir holatda (STW, uzoq)

// Humongous objects — region hajmining 50%+ bo'lgan obyektlar
// Ular to'g'ridan Old Gen ga tushadi!
// -XX:G1HeapRegionSize ni kattalashtirish yordamida oldini olish
}
