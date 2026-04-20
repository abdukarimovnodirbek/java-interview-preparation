
// T ning class ma'lumotini runtime da saqlash uchun:
public class TypedBox<T> {
    private final Class<T> type;    // T ning meta-ma'lumoti
    private T value;

    public TypedBox(Class<T> type) {
        this.type = type;
    }

    public void set(Object value) {
        // Runtime da type check:
        if (!type.isInstance(value)) {
            throw new ClassCastException(
                    "Expected " + type.getName() +
                            ", got " + value.getClass().getName()
            );
        }
        this.value = type.cast(value);
    }

    public T get() {
        return value;
    }

    public Class<T> getType() {
        return type;
    }

    public boolean isType(Object obj) {
        return type.isInstance(obj);
    }
}

void main() {
    // Ishlatish:
    TypedBox<String> box = new TypedBox<>(String.class);
    box.set("Hello");         // ✅
    System.out.println(box.get());         // Hello
    System.out.println(box.getType());     // class java.lang.String

    TypedBox<Integer> intBox = new TypedBox<>(Integer.class);
    intBox.set(42);           // ✅
    intBox.set("wrong");      // ❌ Runtime: ClassCastException
}