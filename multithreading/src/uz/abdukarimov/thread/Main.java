package uz.abdukarimov.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    // Ishlatish:
    static void main() throws InterruptedException, ExecutionException {

        //------------------- CounterThread ---------------------//
        FirstWay t1 = new FirstWay("Thread-A", 1, 5);
        FirstWay t2 = new FirstWay("Thread-B", 6, 10);

        t1.setPriority(Thread.MAX_PRIORITY); // 10 — yuqori prioritet
        t2.setPriority(Thread.MIN_PRIORITY); // 1  — past prioritet

        t1.start(); // ← start() chaqiring, run() emas!
        t2.start();

        t1.join();  // main thread t1 tugashini kutadi
        t2.join();  // main thread t2 tugashini kutadi

        System.out.println("Ikkala thread tugadi!");

        //------------------- FileProcessor ---------------------//
        List<String> results = new ArrayList<>();
        String[] files = {"data.csv", "config.xml", "log.txt", "report.pdf"};

        List<Thread> threads = new ArrayList<>();

        // 1. Class orqali
        for (String file : files) {
            Thread t = new Thread(new SecondWay(file, results));
            threads.add(t);
            t.start();
        }

        // 2. Lambda orqali (Runnable — functional interface)
        Thread lambdaThread = new Thread(() -> {
            System.out.println("Lambda thread ishlayapti: "
                    + Thread.currentThread().getName());
        });
        lambdaThread.start();

        // 3. Anonymous class orqali
        Thread anonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous thread");
            }
        });
        anonThread.start();

        // Barcha thread tugashini kutish
        for (Thread t : threads) t.join();

        System.out.println("\nNatijalar:");
        results.forEach(System.out::println);

        //------------------ PrimeChecker ----------------------//
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Future — kelajakdagi natijani ifodalaydi
        Future<Boolean> f1 = executor.submit(new ThreadWay(17));
        Future<Boolean> f2 = executor.submit(new ThreadWay(20));
        Future<Boolean> f3 = executor.submit(new ThreadWay(97));

        // Boshqa ishlar qilish mumkin — threadlar parallel ishlaydi
        System.out.println("Threadlar ishlayapti...");

        // Natijalarni olish (bloklanadi — thread tugamaguncha)
        System.out.println("17 tub son: " + f1.get());          // true
        System.out.println("20 tub son: " + f2.get());          // false
        System.out.println("97 tub son: " + f3.get());          // true

        // Timeout bilan
        try {
            Boolean result = f1.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Vaqt tugadi!");
            f1.cancel(true); // thread ni to'xtatish
        }

        executor.shutdown();
    }

// Chiqish (tartib kafolatlanmagan):
// [Thread-A] boshlandi
// [Thread-B] boshlandi
// [Thread-A] → 1
// [Thread-B] → 6
// ...

}
