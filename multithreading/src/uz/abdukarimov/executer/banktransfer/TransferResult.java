package uz.abdukarimov.executer.banktransfer;

// ─── Transfer natijasi ──────────────────────────────────────────
public record TransferResult(
        String transferId,
        String fromAccount,
        String toAccount,
        double amount,
        boolean success,
        String message,
        long durationMs
) {
}