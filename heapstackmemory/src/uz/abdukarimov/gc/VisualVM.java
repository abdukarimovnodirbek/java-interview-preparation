package uz.abdukarimov.gc;

public class VisualVM {
/*
# VisualVM ishga tushirish:
jvisualvm          # JDK bilan birga keladi
# yoki yuklab olish: https://visualvm.github.io/

# Qo'shimcha plugin: VisualGC
# Help → Plugins → Available Plugins → VisualGC → Install
*/

// VisualVM bilan nima qilish mumkin:

// 1. MONITOR tab — real-time:
//    CPU %, Heap used/max, Thread count, GC faoliyati

// 2. HEAP DUMP — xotira tahlili:
//    Applications → [dastur] → Heap Dump
//    Keyin: Classes → instance count → References
//    Memory leak topish: ko'p instance bo'lgan klasslar

// 3. CPU PROFILING:
//    Profiler → CPU → Start
//    Dasturni ishlatish → Stop
//    Hot methods: eng ko'p vaqt ketgan metodlar

// 4. MEMORY PROFILING:
//    Profiler → Memory → Start
//    Allocation sites: qayerda ko'p obyekt yaratilmoqda

// 5. THREAD tab:
//    Thread holatlari: Running, Sleeping, Waiting, Monitor
//    Deadlock topish: Thread Dump → "Detect Deadlock"

    // Amaliy: Memory Leak topish misoli
    public class MemoryLeakFinder {

        // VisualVM ko'rsatadigan belgi: bir klass instance soni
        // har GC dan so'ng kamaymasligi

        // 1. VisualVM → Heap Dump ol
        // 2. Classes tabida "byte[]" yoki o'z klasslaringni top
        // 3. Instance count GC dan so'ng ham yuqori bo'lsa → LEAK

        // GC log + VisualVM kombinatsiyasi:
        // GC log → qachon Full GC bo'lganini ko'rsatadi
        // VisualVM → nima memory ushlab turganini ko'rsatadi
    }
}
