package uz.abdukarimov;

public class ThreadExample {
    // Bitta process ichida parallel ishlaydigan mustaqil bajarish oqimi
    // JVM main() dan boshlanadi — bu ham Thread (main thread)

    static void main() {
        Thread current = Thread.currentThread();
        System.out.println(current.getName());     // main
        System.out.println(current.getId());       // 1
        System.out.println(current.getPriority()); // 5
    }

    //         ┌─────────────────────────────────────────────────────┐
    //                 │                                                     │
    //         start()   │   CPU band    sleep()/wait()    notify()/timeout    │
    //         NEW ──────────► RUNNABLE ◄─────── WAITING/TIMED ───────────────────────┘
    //                 │                  WAITING
    //                         │  synchronized
    //                         ├──────────────► BLOCKED ──── lock bo'shasa ──► RUNNABLE
    //                 │
    //                 │  run() tugasa / exception
    //                         └──────────────► TERMINATED
}
