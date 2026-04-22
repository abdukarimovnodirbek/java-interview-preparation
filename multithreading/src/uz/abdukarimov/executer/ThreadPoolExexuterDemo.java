package uz.abdukarimov.executer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExexuterDemo {
    static void main() throws InterruptedException {
        // Custom ThreadFactory — thread nomini boshqarish
        ThreadFactory namedFactory = new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r,
                        "BankPool-" + count.getAndIncrement());
                t.setDaemon(false);          // main tugasa ham ishlaydi
                t.setPriority(Thread.NORM_PRIORITY);
                return t;
            }
        };

        // 4 ta Rejection Policy:
        // AbortPolicy       — RejectedExecutionException (default)
        // CallerRunsPolicy  — calluvchi thread o'zi bajaradi
        // DiscardPolicy     — jim o'tkazib yuboradi
        // DiscardOldestPolicy — queuedagi eng eskini o'chirib, yangini qo'shadi

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,                              // corePoolSize
                6,                              // maximumPoolSize
                30L, TimeUnit.SECONDS,          // keepAliveTime
                new ArrayBlockingQueue<>(20),   // bounded queue
                namedFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // Monitoring
        System.out.println("Active: " + executor.getActiveCount());
        System.out.println("Pool size: " + executor.getPoolSize());
        System.out.println("Queue: " + executor.getQueue().size());
        System.out.println("Completed: " + executor.getCompletedTaskCount());

        // Lifecycle
        executor.shutdown();                        // yangi task qabul qilmaydi
        executor.awaitTermination(60, TimeUnit.SECONDS); // tugashini kutadi
        executor.shutdownNow();                     // hamma threadni interrupt qiladi
    }
}
