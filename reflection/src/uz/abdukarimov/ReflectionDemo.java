package uz.abdukarimov;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

public class ReflectionDemo {
    // ── Fieldlar ─────────────────────────────────────────────────
    public static class Person {
        public String name;
        private int age;
        protected String city;
    }

    static void main() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        // ── Class obyektini olish ─────────────────────────────────────
        Class<String> c1 = String.class;           // literal
        Class<?> c2 = "salom".getClass();          // instance dan
        Class<?> c3 = Class.forName("java.lang.String"); // nom bilan

// ── Klass ma'lumotlari ────────────────────────────────────────
        System.out.println(c1.getName());          // java.lang.String
        System.out.println(c1.getSimpleName());    // String
        System.out.println(c1.getPackageName());   // java.lang
        System.out.println(c1.isInterface());      // false
        System.out.println(c1.isRecord());         // false
        System.out.println(c1.getSuperclass());    // class java.lang.Object

        Class<Person> pc = Person.class;

// getFields()        → public fieldlar (inherited ham)
// getDeclaredFields()→ bu klassning BARCHA fieldlari
        Field[] fields = pc.getDeclaredFields();
        for (Field f : fields) {
            System.out.printf("%-20s %-10s %s%n",
                    f.getName(),
                    f.getType().getSimpleName(),
                    Modifier.toString(f.getModifiers()));
        }
// name                String     public
// age                 int        private
// city                String     protected

// Private fieldni o'qish/yozish:
        Person p = new Person();
        Field ageField = pc.getDeclaredField("age");
        ageField.setAccessible(true);   // access ochish
        ageField.set(p, 25);
        System.out.println(ageField.get(p)); // 25

// ── Metodlar ─────────────────────────────────────────────────
        Method[] methods = pc.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println(m.getName() + " → " +
                    Arrays.toString(m.getParameterTypes()));
        }

// Metod chaqirish:
        Method greet = pc.getDeclaredMethod("greet", String.class);
        greet.setAccessible(true);
        String result = (String) greet.invoke(p, "Ali");

// ── Constructor ───────────────────────────────────────────────
        Constructor<Person> ctor =
                pc.getDeclaredConstructor(String.class, int.class);
        ctor.setAccessible(true);
        Person p2 = ctor.newInstance("Vali", 30);

// ── Generic type ma'lumoti ────────────────────────────────────
        class Container<T> {
            List<String> items;
        }
        Field itemsField = Container.class.getDeclaredField("items");
        ParameterizedType pt =
                (ParameterizedType) itemsField.getGenericType();
        Type typeArg = pt.getActualTypeArguments()[0];
        System.out.println(typeArg); // class java.lang.String
    }
}
