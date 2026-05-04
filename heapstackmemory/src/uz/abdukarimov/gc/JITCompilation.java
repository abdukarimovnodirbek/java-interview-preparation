package uz.abdukarimov.gc;

public class JITCompilation {
// JIT optimizatsiyalarini ko'rish
// -XX:+PrintCompilation  → kompilyatsiya logini chiqarish
// -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining → inlining log

// JIT optimallashtirish turlari:

    // 1. Inlining — kichik metodlarni chaqiruvchi joyiga "yozib qo'yish"
// Avval:
/*
    int result = add(a, b);  // metod chaqiruvi = overhead

    // JIT inline qilgandan so'ng:
    int result = a + b;       // to'g'ridan bajariladi

    // 2. Escape Analysis — obyekt heap ga bormasligi mumkin
    public int sum() {
        Point p = new Point(1, 2);  // p faqat bu metodda ishlatilsa
        return p.x + p.y;           // JIT stack da yoki registrda saqlaydi!
    }                                // heap allocation yo'q → GC yuki kamayadi
*/

// 3. Loop unrolling
// Avval:for
/*
for (int i = 0; i < 4; i++) sum += arr[i];

// JIT dan so'ng:
    sum += arr[0]; sum += arr[1]; sum += arr[2]; sum += arr[3];

// 4. Intrinsics — JVM maxsus native implementatsiya ishlatadi
Math.sqrt()       // CPU SQRT instruksiyasiga aylanadi
        System.arraycopy() // SIMD bilan tezlashtirilgan
        String.equals()   // optimallashtirilgan
*/

// JIT tuning flaglari:
// -XX:CompileThreshold=10000   → necha marta chaqirilsa kompilyatsiya
// -XX:+TieredCompilation       → Tiered on (Java 8+ default)
// -XX:-TieredCompilation       → faqat C2 (server mode)
// -Xcomp                       → barcha metodlarni darhol kompilyatsiya
// -Xint                        → faqat interpreter (benchmark uchun)
}
