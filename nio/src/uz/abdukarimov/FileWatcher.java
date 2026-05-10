package uz.abdukarimov;

import java.io.IOException;
import java.nio.file.*;

// WatchService — Papka o'zgarishini kuzatish
public class FileWatcher {

    public static void watchDirectory(Path dir)
            throws IOException, InterruptedException {

        WatchService watchService =
                FileSystems.getDefault().newWatchService();

        dir.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        System.out.println("Kuzatilmoqda: " + dir);

        while (true) {
            WatchKey key = watchService.take(); // bloklaydi

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW)
                    continue;

                @SuppressWarnings("unchecked")
                WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                Path changed = dir.resolve(pathEvent.context());

                String action = switch (kind.name()) {
                    case "ENTRY_CREATE" -> "YARATILDI";
                    case "ENTRY_DELETE" -> "O'CHIRILDI";
                    case "ENTRY_MODIFY" -> "O'ZGARTIRILDI";
                    default -> kind.name();
                };

                System.out.printf("[%s] %s%n", action, changed);
            }

            boolean valid = key.reset();
            if (!valid) break;
        }
    }

    static void main() throws Exception {
        Path dir = Path.of(System.getProperty("user.home"));
        watchDirectory(dir);
    }
}