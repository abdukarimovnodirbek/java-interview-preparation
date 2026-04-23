package uz.abdukarimov.racecondition;

public class VolatileDemo {
    // ── volatile OLMASA — muammo ───────────────────────────────
    static boolean running = true; // CPU cache da qolishi mumkin!

    // ── volatile BILAN — kafolatlangan ────────────────────────
    static volatile boolean runningV = true; // har doim main memoryd'an o'qiladi

    public static void main(String[] args) throws InterruptedException {

        // Muammo: thread running ni cache dan o'qiydi
        // volatile bo'lmasa — cheksiz loop bo'lishi mumkin!
        Thread worker = new Thread(() -> {
            int i = 0;
            while (runningV) {   // volatile: har doim fresh qiymat
                i++;
            }
            System.out.println("To'xtadi, i = " + i);
        });

        worker.start();
        Thread.sleep(100);
        runningV = false;       // volatile: worker darhol ko'radi
        worker.join();
    }
}

//  volatile YO'Q:                    volatile BILAN:
//  ┌────────────┐                    ┌────────────┐
//  │  Thread 1  │                    │  Thread 1  │
//  │ cache: x=5 │ ← stale!           │  cache: —  │ ← cache yo'q
//  └────────────┘                    └─────┬──────┘
//        ↕ (ba'zan)                        ↕ (har doim)
//  ┌────────────┐                    ┌─────┴──────┐
//  │Main memory │                    │Main memory │
//  │    x = 7   │                    │    x = 7   │
//  └────────────┘                    └─────┬──────┘
//        ↕ (ba'zan)                        ↕ (har doim)