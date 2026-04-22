package uz.abdukarimov.thread;

public class ThreadMethods {
    static void main() throws InterruptedException {
        Thread t = new Thread(() -> { /* ish */ });

        // Boshqarish
        t.start();            // yangi thread boshlash
        t.join();             // shu thread tugaguncha kutish
        t.join(2000);         // max 2 soniya kutish
        t.interrupt();        // interrupt signal yuborish
        t.isAlive();          // ishlayaptimi?
        t.isInterrupted();    // interrupt belgisi bormi?

        // Ma'lumot
        t.getName();          // "Thread-0"
        t.getId();            // 12
        t.getState();         // State enum
        t.getPriority();      // 1-10 (default: 5)

        // Static metodlar
        Thread.sleep(1000);          // joriy threadni 1 sek to'xtatish
        Thread.yield();              // CPU ni boshqalarga berish
        Thread.currentThread();      // joriy thread
        Thread.activeCount();        // aktiv thread soni
        Thread.interrupted();        // interrupt belgisini tekshir va tozala

        // Daemon thread — JVM yopilsa u ham yopiladi
        t.setDaemon(true);   // start() DAN OLDIN chaqirish shart!
        t.isDaemon();
    }
}
