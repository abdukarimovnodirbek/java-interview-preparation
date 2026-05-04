package uz.abdukarimov.memoryleak;

// Sabab 5 — Inner class ning tashqi klassga referenci
public class MemoryLeak5 {
    // ❌ MUAMMO: non-static inner class tashqi klassni ushlab turadi
    public class Outer {
        private byte[] largeData = new byte[10 * 1024 * 1024]; // 10MB

        // Non-static inner class — Outer.this ga implicit reference!
        class Inner {
            void doWork() { /* largeData ishlatilmasa ham */ }
        }
    }

    // Outer release qilinsa ham, Inner tirik bo'lsa → largeData = LEAK!
/*
    List<Outer.Inner> inners = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
        inners.add(new Outer().new Inner()); // 100 × 10MB = 1GB!
    }
*/

    // ✅ YECHIM: static inner class
    public class Outer1 {
        private byte[] largeData = new byte[10 * 1024 * 1024];

        static class Inner {  // static — Outer.this referenci yo'q!
            void doWork() {
            }
        }
    }
}
