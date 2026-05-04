package uz.abdukarimov.memoryleak;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

// Sabab 7 — ClassLoader Leak (Metaspace)
public class MemoryLeak7 {
    // ❌ MUAMMO: har request uchun yangi URLClassLoader yaratib,
//            yopilmaydi → klass meta-ma'lumotlari Metaspace da qoladi
    public void loadPlugin1(String jarPath) throws Exception {
        URLClassLoader loader = new URLClassLoader(
                new URL[]{new File(jarPath).toURI().toURL()}
        );
        Class<?> plugin = loader.loadClass("MyPlugin");
        // loader.close() chaqirilmadi → Metaspace leak!
    }

    // ✅ YECHIM: ClassLoader ni yopish
    public void loadPlugin(String jarPath) throws Exception {
        try (URLClassLoader loader = new URLClassLoader(
                new URL[]{new File(jarPath).toURI().toURL()})) {
            Class<?> plugin = loader.loadClass("MyPlugin");
            plugin.getDeclaredConstructor().newInstance();
        } // close() — loader va yuklangan klasslar ozod qilinadi
    }
}
