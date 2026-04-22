package uz.abdukarimov.thread;

import java.util.List;

// Runnable — @FunctionalInterface, bitta metod: run()
public class SecondWay implements Runnable {

    private final String fileName;
    private final List<String> results;

    public SecondWay(String fileName, List<String> results) {
        this.fileName = fileName;
        this.results = results;
    }

    @Override
    public void run() {
        System.out.println("Fayl o'qilmoqda: " + fileName);
        try {
            Thread.sleep(500); // fayl o'qishni simulate qilish
            synchronized (results) {
                results.add(fileName + " — OK");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}