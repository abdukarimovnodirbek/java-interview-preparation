package uz.abdukarimov.stack;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class GenericStack<T> {

    private Object[] elements;     // T[] yaratib bo'lmagani uchun
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;

    // ─── Constructor ────────────────────────────────────────────
    public GenericStack() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public GenericStack(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity > 0 bo'lishi shart");
        elements = new Object[capacity];
    }

    // ─── Push ───────────────────────────────────────────────────
    public void push(T item) {
        if (item == null) throw new NullPointerException("null qo'shib bo'lmaydi");
        ensureCapacity();
        elements[size++] = item;
    }

    // ─── Pop ────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")    // erasure sababli kerak
    public T pop() {
        if (isEmpty()) throw new EmptyStackException();
        T item = (T) elements[--size];
        elements[size] = null;        // xotira leak oldini olish!
        return item;
    }

    // ─── Peek ───────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new EmptyStackException();
        return (T) elements[size - 1];
    }

    // ─── Search ─────────────────────────────────────────────────
    // Tepadan qidiradi, 1-based index qaytaradi (-1 = topilmadi)
    public int search(T item) {
        for (int i = size - 1; i >= 0; i--) {
            if (elements[i].equals(item)) {
                return size - i;      // tepadan masofasi
            }
        }
        return -1;
    }

    // ─── Utility metodlar ───────────────────────────────────────
    public boolean isEmpty()  { return size == 0; }
    public int size()         { return size; }

    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    // ─── Capacity kengaytirish ───────────────────────────────────
    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, size * 2);
        }
    }

    // ─── Iterator ────────────────────────────────────────────────
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = size - 1;   // tepadan pastga

            @Override public boolean hasNext() { return index >= 0; }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (T) elements[index--];
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stack[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("] ← top").toString();
    }
}