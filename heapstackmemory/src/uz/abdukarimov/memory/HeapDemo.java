package uz.abdukarimov.memory;

import java.util.ArrayList;
import java.util.List;

public class HeapDemo {
    // Obyekt yaratilganda nima bo'ladi?

// 1. new Obj() → Eden ga tushadi
//    Eden to'lsa → Minor GC
//    Tirik obyektlar → Survivor 0 ga ko'chiriladi (age=1)

// 2. Keyingi Minor GC:
//    Tirik S0 obyektlari → S1 ga (age=2)
//    Eden tirik obyektlari → S1 ga (age=1)
//    S0 va Eden tozalanadi

// 3. age > 15 (default) → Old Gen ga promote qilinadi
//    -XX:MaxTenuringThreshold=15 bilan sozlanadi

// 4. Old Gen to'lsa → Major GC (uzoqroq, "Stop-the-World")

    public static class HeapExample {
        static void main() {
            // Eden da yaratiladi
            String s = new String("salom");

            // Long-lived obyekt → Old Gen ga o'tadi
            List<byte[]> cache = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                cache.add(new byte[1024 * 1024]); // 1MB × 100 = 100MB
            }
        }
    }
}

/*
Obyekt hayot sikli:

        new Obj() → Eden (age=0)
         ↓ Minor GC
Survivor 0 (age=1)
        ↓ Minor GC
Survivor 1 (age=2)
        ↓ ... (age=15 ga yetganda)
Old Generation
         ↓ Old Gen to'lganda
Major / Full GC → Stop-the-World
*/
