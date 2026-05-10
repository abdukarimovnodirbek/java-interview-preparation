package uz.abdukarimov.example;

import uz.abdukarimov.FileWatcher;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

// ── Serializer ────────────────────────────────────────────────
public class JsonSerializer {

    public static String serialize(Object obj)
            throws IllegalAccessException {

        Class<?> clazz = obj.getClass();

        if (!clazz.isAnnotationPresent(JsonSerializable.class)) {
            throw new IllegalArgumentException(
                    clazz.getSimpleName() +
                            " @JsonSerializable annotatsiyasi yo'q");
        }

        StringBuilder json = new StringBuilder("{\n");
        boolean first = true;

        // Record komponentlari uchun
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            // @JsonField bo'lmasa ham serializatsiya
            JsonField ann = field.getAnnotation(JsonField.class);

            // ignore=true bo'lsa o'tkazib yuborish
            if (ann != null && ann.ignore()) continue;

            // required tekshiruvi
            Object value = field.get(obj);
            if (ann != null && ann.required() && value == null) {
                throw new IllegalStateException(
                        field.getName() + " required lekin null");
            }

            if (!first) json.append(",\n");
            first = false;

            // Nom: annotatsiyadan yoki field nomi
            String jsonName = (ann != null && !ann.name().isEmpty())
                    ? ann.name()
                    : field.getName();

            json.append("  \"").append(jsonName).append("\": ");

            // Qiymat serializatsiyasi
            if (value == null) {
                json.append("null");
            } else if (value instanceof String s) {
                json.append("\"")
                        .append(s.replace("\"", "\\\""))
                        .append("\"");
            } else if (value instanceof Number ||
                    value instanceof Boolean) {
                json.append(value);
            } else {
                json.append("\"").append(value).append("\"");
            }
        }

        json.append("\n}");
        return json.toString();
    }

    // NIO bilan faylga yozish
    public static void saveToFile(Object obj, Path path)
            throws Exception {
        String json = serialize(obj);
        Files.writeString(path, json,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Saqlandi: " + path);
    }

    // NIO bilan fayldan o'qish (metadata)
    public static void printFileInfo(Path path)
            throws IOException {
        if (!Files.exists(path)) {
            System.out.println("Fayl topilmadi: " + path);
            return;
        }
        System.out.printf("""
                        Fayl: %s
                        Hajm: %d bayt
                        O'zgartirilgan: %s
                        Mazmun:
                        %s
                        """,
                path.getFileName(),
                Files.size(path),
                Files.getLastModifiedTime(path),
                Files.readString(path));
    }

    // ── Main ──────────────────────────────────────────────────────
    static void main() throws Exception {

        Product product = new Product(
                "MacBook Pro",
                2499.99,
                "INTERNAL-XYZ",
                10
        );

        // Serializatsiya
        String json = JsonSerializer.serialize(product);
        System.out.println(json);
    /*
    {
      "product_name": "MacBook Pro",
      "price": 2499.99,
      "stock": 10
    }
    // internalCode ignore=true bo'lgani uchun yo'q
    */

        // Faylga yozish
        Path path = Path.of("product.json");
        JsonSerializer.saveToFile(product, path);

        // Fayl ma'lumotlari
        JsonSerializer.printFileInfo(path);

        // WatchService — o'zgarishlarni kuzatish
        Path dir = path.toAbsolutePath().getParent();
        Thread watcher = new Thread(() -> {
            try {
                FileWatcher.watchDirectory(dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        watcher.setDaemon(true);
        watcher.start();

        // Test uchun fayl o'zgartirish
        Thread.sleep(500);
        Files.writeString(path, "// yangilandi",
                StandardOpenOption.APPEND);
    }
}