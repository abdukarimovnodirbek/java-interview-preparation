package uz.abdukarimov;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

// NIO.2 — Files va Path API
public class Nio2Demo {
    static void main() throws IOException {
// ── Path — yo'l operatsiyalari ────────────────────────────────
        Path p1 = Path.of("/home/user/docs/file.txt");
        Path p2 = Paths.get("relative", "path", "file.txt");

        p1.getFileName();            // file.txt
        p1.getParent();              // /home/user/docs
        p1.getRoot();                // /
        p1.toAbsolutePath();         // mutlaq yo'l
        p1.normalize();              // ./a/../b → /b
        p1.resolve("other.txt");     // /home/user/docs/other.txt
        p1.relativize(Path.of("/home/user")); // ../..

// ── Files — fayl operatsiyalari ───────────────────────────────

// Asosiy tekshiruvlar:
        Files.exists(p1);
        Files.isDirectory(p1);
        Files.isReadable(p1);
        Files.size(p1);                       // bayt
        Files.getLastModifiedTime(p1);

// O'qish (kichik fayllar):
        String content = Files.readString(p1, StandardCharsets.UTF_8);
        List<String> lines = Files.readAllLines(p1);
        byte[] bytes = Files.readAllBytes(p1);

// Yozish:
        Files.writeString(p1, "Mazmun", StandardCharsets.UTF_8);
        Files.write(p1, bytes);
        Files.write(p1, lines,
                StandardOpenOption.APPEND);       // qo'shib yozish

// Nusxalash va ko'chirish:
//        Files.copy(src, dest,
//                StandardCopyOption.REPLACE_EXISTING,
//                StandardCopyOption.COPY_ATTRIBUTES);
//        Files.move(src, dest,
//                StandardCopyOption.REPLACE_EXISTING,
//                StandardCopyOption.ATOMIC_MOVE);

// Yaratish va o'chirish:
        Files.createFile(p1);
        Files.createDirectories(p1);         // barcha ota-dirlarni ham
        Files.delete(p1);
        Files.deleteIfExists(p1);

// ── Stream bilan fayl o'qish (katta fayllar) ──────────────────
        try (Stream<String> stream = Files.lines(
                Path.of("bigfile.txt"), StandardCharsets.UTF_8)) {
            long count = stream
                    .filter(line -> line.contains("ERROR"))
                    .count();
            System.out.println("Xato qatorlar: " + count);
        }

// ── Papkani aylantirish ───────────────────────────────────────
// list() — faqat to'g'ridan farzandlar
        try (Stream<Path> stream = Files.list(Path.of("/home/user"))) {
            stream.filter(Files::isRegularFile)
                    .forEach(System.out::println);
        }

// walk() — rekursiv, barcha farzandlar
        try (Stream<Path> stream = Files.walk(Path.of("/project"))) {
            stream.filter(p -> p.toString().endsWith(".java"))
                    .map(Path::getFileName)
                    .forEach(System.out::println);
        }

// find() — predicate bilan
        try (Stream<Path> stream = Files.find(
                Path.of("/project"), 5, // max depth
                (path, attrs) ->
                        attrs.isRegularFile() &&
                                path.toString().endsWith(".java") &&
                                attrs.size() > 1000)) {
            stream.forEach(System.out::println);
        }
    }
}
