package uz.abdukarimov.executer.banktransfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

// ─── Main — simulatsiya ─────────────────────────────────────────
public class BankSimulation {

    static void main() throws Exception {

        // Hisoblar yaratish
        Map<String, BankAccount> accounts = new ConcurrentHashMap<>();
        accounts.put("ACC001", new BankAccount("ACC001", "Ali", 5_000_000));
        accounts.put("ACC002", new BankAccount("ACC002", "Vali", 3_000_000));
        accounts.put("ACC003", new BankAccount("ACC003", "Sali", 8_000_000));
        accounts.put("ACC004", new BankAccount("ACC004", "Dilnoza", 2_000_000));
        accounts.put("ACC005", new BankAccount("ACC005", "Jamshid", 6_000_000));

        BankTransferService bank = new BankTransferService(accounts);

        System.out.println("=== Bank Transfer Simulatsiyasi ===\n");
        printBalances(accounts);

        // 15 ta parallel transfer
        List<Future<TransferResult>> futures = new ArrayList<>();

        futures.add(bank.transferAsync("ACC001", "ACC002", 500_000));
        futures.add(bank.transferAsync("ACC003", "ACC001", 1_000_000));
        futures.add(bank.transferAsync("ACC002", "ACC004", 200_000));
        futures.add(bank.transferAsync("ACC005", "ACC003", 2_000_000));
        futures.add(bank.transferAsync("ACC004", "ACC005", 100_000));
        futures.add(bank.transferAsync("ACC001", "ACC003", 3_000_000));// yetmaydi
        futures.add(bank.transferAsync("ACC002", "ACC001", 800_000));
        futures.add(bank.transferAsync("ACC003", "ACC005", 1_500_000));
        futures.add(bank.transferAsync("ACC001", "ACC004", -100_000)); // noto'g'ri
        futures.add(bank.transferAsync("ACC005", "ACC002", 400_000));
        futures.add(bank.transferAsync("ACC004", "ACC001", 300_000));
        futures.add(bank.transferAsync("ACC002", "ACC003", 250_000));
        futures.add(bank.transferAsync("ACC003", "ACC004", 750_000));
        futures.add(bank.transferAsync("ACC001", "ACC005", 600_000));
        futures.add(bank.transferAsync("ACC999", "ACC001", 100_000));// topilmaydi

        // Pool holatini monitoring qilish
        bank.printStats();

        // Barcha natijalarni yig'ish
        System.out.println("\n=== Transfer Natijalari ===");
        int success = 0, failed = 0;

        for (Future<TransferResult> f : futures) {
            TransferResult r = f.get(); // natijani kutish
            String status = r.success() ? "✅" : "❌";
            System.out.printf("%s [%s] %s (%dms)%n",
                    status, r.transferId(), r.message(), r.durationMs());
            if (r.success()) success++;
            else failed++;
        }

        // Yakuniy hisobot
        System.out.println("\n=== Yakuniy Balanslar ===");
        printBalances(accounts);

        System.out.printf("%n✅ Muvaffaqiyatli: %d | ❌ Rad etilgan: %d%n",
                success, failed);

        // Jami mablag' o'zgarmagan bo'lishi kerak!
        double total = accounts.values().stream()
                .mapToDouble(BankAccount::getBalance).sum();
        System.out.printf("Jami mablag': %.0f so'm%n", total);
        // Har doim: 24_000_000 so'm

        bank.shutdown();
    }

    static void printBalances(Map<String, BankAccount> accounts) {
        System.out.println("--- Balanslar ---");
        accounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> System.out.printf("  [%s] %-10s : %,10.0f so'm%n",
                        e.getKey(), e.getValue().getOwner(),
                        e.getValue().getBalance()));
        System.out.println();
    }
}