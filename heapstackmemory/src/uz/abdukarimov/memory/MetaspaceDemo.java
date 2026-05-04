package uz.abdukarimov.memory;

public class MetaspaceDemo {
// PermGen (Java 7 va oldin):
//   - Heap ichida, belgilangan o'lcham
//   - java.lang.OutOfMemoryError: PermGen space

// Metaspace (Java 8+):
//   - Native memory da — Heap TASHQARISIDA
//   - Default: cheksiz (OS xotirasi tugaguncha)
//   - -XX:MaxMetaspaceSize=256m bilan cheklash

// Metaspace nima saqlaydi?
//   - Klass ta'riflari (bytecode)
//   - Method metadata
//   - Static o'zgaruvchilar (Java 8+ da Heap da saqlangan bo'lishi ham mumkin)
//   - Constant pool

// Monitoring:
// jmap -heap <pid>    → Metaspace ishlatilishini ko'rish
// jstat -gcmetacapacity <pid>  → Metaspace sig'imi
}
