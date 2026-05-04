package uz.abdukarimov.memory;

// ── OutOfMemoryError turlari ───────────────────────────────────

// 1. java.lang.OutOfMemoryError: Java heap space
//    Sabab: Heap to'lib ketdi
//    Yechim: -Xmx oshirish YOKI memory leak topish

// 2. java.lang.OutOfMemoryError: GC overhead limit exceeded
//    Sabab: GC vaqtining 98% dan ko'pi xotira tozalashga ketayapti
//    Yechim: Memory leak topish, -Xmx oshirish

// 3. java.lang.OutOfMemoryError: Metaspace
//    Sabab: Juda ko'p klass yuklandi (ClassLoader leak)
//    Yechim: -XX:MaxMetaspaceSize=512m YOKI ClassLoader leak topish

// 4. java.lang.StackOverflowError
//    Sabab: Rekursiya juda chuqur
//    Yechim: -Xss2m YOKI rekursiyani iteratsiyaga aylantirish

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

// ── Memory Leak topish ─────────────────────────────────────────
public class MemoryLeakExample {

    // ❌ Memory Leak — static listga qo'shib, hech narsani o'chirmaydi
    private static final List<byte[]> LEAK = new ArrayList<>();

    public static void simulateLeak() {
        while (true) {
            LEAK.add(new byte[1024 * 1024]); // 1MB qo'shib ketaveradi
            // LEAK.clear() hech qachon chaqirilmaydi!
        }
    }

    // ✅ To'g'ri — weak reference ishlatish
    private static final List<WeakReference<byte[]>> SAFE =
            new ArrayList<>();

    public static void safeCaching() {
        SAFE.add(new WeakReference<>(new byte[1024 * 1024]));
        // GC kerak bo'lganda WeakReference ni tozalaydi
    }
}

// ── Heap Dump olish ───────────────────────────────────────────
// 1. Avtomatik: -XX:+HeapDumpOnOutOfMemoryError
//              -XX:HeapDumpPath=/tmp/heap.hprof

// 2. Qo'lda:
//    jmap -dump:format=b,file=heap.hprof <pid>
//    jcmd <pid> GC.heap_dump heap.hprof

// 3. Tahlil: Eclipse MAT, VisualVM, IntelliJ Profiler
