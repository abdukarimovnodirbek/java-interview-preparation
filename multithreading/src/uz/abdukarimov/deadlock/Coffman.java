package uz.abdukarimov.deadlock;

public class Coffman {
    static void main() {
        // 1.
        /*Muammo: Resurs bir vaqtda faqat bitta thread tomonidan ishlatilishi mumkin. Boshqa threadlar kutadi.
        Yechim: Imkon bo'lsa resursni immutable qiling — o'qish faqat operatsiyalar uchun lock kerak emas. ReadWriteLock bilan o'qishni parallellashtiring.
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        rwLock.readLock().lock();  // ko'p thread parallel o'qiydi*/

        // 2.
        /* Muammo: Thread allaqachon bir resursni ushlab turib, boshqa resursni kutadi. Ushlaganini qo'ymaydi.
         Yechim: Barcha resurslarni bir vaqtda oling yoki birontasini ololmasangiz — barchasini qo'ying.
         Barcha lockni bir atomik operatsiyada oling:
        if (lock1.tryLock() && lock2.tryLock()) {
            try { *//* ish *//* }
            finally { lock1.unlock(); lock2.unlock(); }
        } else {
            // ikkala lock ham bo'shatiladi
        }*/

        // 3.
        /*Muammo: Resursni faqat uni ushlab turgan thread qo'ya oladi. Tashqi kuch resursni tortib ololmaydi.
        Yechim: tryLock(timeout) ishlatib, ololmaganida o'zingiz resursni qo'ying.
        if (!lock.tryLock(500, TimeUnit.MILLISECONDS)) {
            // ololmadim — qo'lda bo'shataYman
            existingLock.unlock();
            Thread.sleep(rand.nextInt(100));
            // qayta urinaman
        }*/

        // 4.
        /*Muammo: T1 → L2 kutadi, T2 → L1 kutadi. Aylana hosil bo'ladi. Eng keng tarqalgan sabab.
        Yechim: Barcha locklar uchun global tartib belgilang. Har doim bir xil tartibda oling.
        // Kichik ID ni DOIM birinchi oling:
        Object first  = id1 < id2 ? lock1 : lock2;
        Object second = id1 < id2 ? lock2 : lock1;

        synchronized(first) {
            synchronized(second) { *//* ish *//* }
        }*/
    }
}
