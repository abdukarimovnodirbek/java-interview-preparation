package uz.abdukarimov.thread;

import java.util.concurrent.Callable;

// Callable<V> — bitta metod: call() → V
public class ThreadWay implements Callable<Boolean> {

    private final int number;

    public ThreadWay(int number) {
        this.number = number;
    }

    @Override
    public Boolean call() throws Exception {
        System.out.println(number + " tekshirilmoqda... ["
                + Thread.currentThread().getName() + "]");
        Thread.sleep(300);
        return isPrime(number);
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}