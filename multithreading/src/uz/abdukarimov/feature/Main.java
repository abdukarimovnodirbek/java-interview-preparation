package uz.abdukarimov.feature;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    static void main() throws InterruptedException, ExecutionException {
        ExecutorService exec = Executors.newCachedThreadPool();

        Future<String> future = exec.submit(() -> {
            Thread.sleep(1000);
            return "Natija tayyor!";
        });

        // isDone() — tugadimi?
        while (!future.isDone()) {
            System.out.println("Kutilmoqda...");
            Thread.sleep(200);
        }

        // get() — natijani olish (bloklaydi)
        String result = future.get(); // "Natija tayyor!"

        // cancel() — bekor qilish
        future.cancel(true);  // true = interrupt yuborish
        future.isCancelled(); // bekor qilinganmi?

        exec.shutdown();
    }
}
