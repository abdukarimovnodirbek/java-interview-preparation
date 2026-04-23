package uz.abdukarimov.racecondition;

public class Main {
//  ┌──────────────────┬────────────┬───────────────┬──────────────────┐
//  │                  │ synchronized│ ReentrantLock │ volatile         │
//  ├──────────────────┼────────────┼───────────────┼──────────────────┤
//  │ Atomiklik        │    ✅      │      ✅       │       ❌         │
//  │ Visibility       │    ✅      │      ✅       │       ✅         │
//  │ tryLock          │    ❌      │      ✅       │       —          │
//  │ Timeout          │    ❌      │      ✅       │       —          │
//  │ Interrupt        │    ❌      │      ✅       │       —          │
//  │ Bir nechta Cond. │    ❌      │      ✅       │       —          │
//  │ Soddalik         │    ✅      │      ❌       │       ✅         │
//  │ Ishlatish        │  Oddiy lock│ Murakkab lock │ Flag/singleton   │
//  └──────────────────┴────────────┴───────────────┴──────────────────┘
//
//  Qoida:
//    Oddiy lock kerak         → synchronized
//    tryLock / timeout kerak  → ReentrantLock
//    Ko'p o'qish, kam yozish  → ReadWriteLock
//    Faqat visibility kerak   → volatile
//    Counter / flag           → AtomicInteger / AtomicBoolean

}
