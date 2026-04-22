package uz.abdukarimov.thread;

public class FirstWay extends Thread {
    private final String threadName;
    private final int    from;
    private final int    to;

    public FirstWay(String name, int from, int to) {
        super(name);            // Thread nomini berish
        this.threadName = name;
        this.from       = from;
        this.to         = to;
    }

    @Override
    public void run() {
        System.out.printf("[%s] boshlandi%n", threadName);
        for (int i = from; i <= to; i++) {
            System.out.printf("[%s] → %d%n", threadName, i);
            try {
                Thread.sleep(200); // 200ms kutish
            } catch (InterruptedException e) {
                System.out.println(threadName + " to'xtatildi!");
                Thread.currentThread().interrupt(); // flag qayta o'rnatish
                return;
            }
        }
        System.out.printf("[%s] tugadi%n", threadName);
    }
}