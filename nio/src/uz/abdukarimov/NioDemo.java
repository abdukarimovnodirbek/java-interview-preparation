package uz.abdukarimov;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

// NIO Buffer va Channel
public class NioDemo {
    static void main() throws IOException {
// ── Buffer — ma'lumot konteyner ───────────────────────────────
// capacity → jami sig'im
// limit    → o'qish/yozish chegarasi
// position → joriy joylashuv
// mark     → qaytish nuqtasi

        ByteBuffer buf = ByteBuffer.allocate(1024); // heap da
        ByteBuffer dir = ByteBuffer.allocateDirect(1024); // native memory

// Yozish rejimi (position 0 dan boshlanadi):
        buf.put((byte) 65);   // 'A'
        buf.put((byte) 66);   // 'B'
        buf.put((byte) 67);   // 'C'
// position=3, limit=1024, capacity=1024

// O'qish rejimiga o'tish:
        buf.flip();
// position=0, limit=3, capacity=1024

        while (buf.hasRemaining()) {
            System.out.print((char) buf.get()); // ABC
        }

        buf.rewind();  // position=0, limit o'zgarmaydi
        buf.clear();   // position=0, limit=capacity

// ── FileChannel — fayl bilan ishlash ─────────────────────────
// O'qish:
        try (FileChannel ch =
                     FileChannel.open(Path.of("data.txt"),
                             StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            int bytesRead;
            StringBuilder sb = new StringBuilder();
            while ((bytesRead = ch.read(buffer)) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    sb.append((char) buffer.get());
                }
                buffer.clear();
            }
            System.out.println(sb);
        }

// Yozish:
        try (FileChannel ch = FileChannel.open(
                Path.of("output.txt"),
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            String text = "Salom NIO dunyo!";
            ByteBuffer buffer = ByteBuffer.wrap(
                    text.getBytes(StandardCharsets.UTF_8));
            while (buffer.hasRemaining()) {
                ch.write(buffer);
            }
        }

// ── transferTo — tez fayl nusxalash ──────────────────────────
// OS darajasida zero-copy — juda tez!
        try (FileChannel src = FileChannel.open(Path.of("source.txt"),
                StandardOpenOption.READ);
             FileChannel dest = FileChannel.open(Path.of("copy.txt"),
                     StandardOpenOption.WRITE,
                     StandardOpenOption.CREATE)) {
            src.transferTo(0, src.size(), dest);
        }

// ── MappedByteBuffer — memory-mapped fayl ─────────────────────
// Katta fayllar uchun — OS virtual memory bilan ishlaydi
        try (FileChannel ch = FileChannel.open(Path.of("bigfile.bin"),
                StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            MappedByteBuffer mmap = ch.map(
                    FileChannel.MapMode.READ_WRITE, 0, ch.size());

            // To'g'ridan xotira kabi ishlash
            mmap.position(100);
            mmap.putInt(42);         // faylga yoziladi
            mmap.position(100);
            int value = mmap.getInt(); // fayldan o'qiladi
        }
    }
}
