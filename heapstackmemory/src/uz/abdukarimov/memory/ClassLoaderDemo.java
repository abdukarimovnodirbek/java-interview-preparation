package uz.abdukarimov.memory;

// ClassLoader ni kodda ko'rish
public class ClassLoaderDemo {
    static void main() {

        // Application ClassLoader
        ClassLoader appCL = ClassLoaderDemo.class.getClassLoader();
        System.out.println(appCL);
        // jdk.internal.loader.ClassLoaders$AppClassLoader

        // Platform ClassLoader
        System.out.println(appCL.getParent());
        // jdk.internal.loader.ClassLoaders$PlatformClassLoader

        // Bootstrap ClassLoader — null qaytaradi (C++ da)
        System.out.println(appCL.getParent().getParent()); // null

        // String — Bootstrap yuklaydi
        System.out.println(String.class.getClassLoader()); // null

        // Custom ClassLoader yozish
        ClassLoader custom = new ClassLoader(appCL) {
            @Override
            protected Class<?> findClass(String name)
                    throws ClassNotFoundException {
                // .class faylni o'qib Class ga aylantirish
                byte[] classBytes = loadBytesFromDisk(name);
                return defineClass(name, classBytes, 0,
                        classBytes.length);
            }

            private byte[] loadBytesFromDisk(String name) {
                // fayl tizimidan yoki network dan o'qish
                return new byte[0]; // stub
            }
        };
    }
}