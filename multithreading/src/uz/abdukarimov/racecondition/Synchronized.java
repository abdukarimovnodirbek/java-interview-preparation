package uz.abdukarimov.racecondition;

public class Synchronized {
    // Test — 2 thread parallel increment qiladi
    static void main() throws InterruptedException {
        Counter c = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) c.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) c.increment();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(c.getCount()); // har doim: 20000
    }
}
