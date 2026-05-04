package uz.abdukarimov.memory;

public class StackDemo {

    // Har bir metod chaqirovi = yangi Stack Frame
    // Frame ichida: local variables, operand stack, frame data

    public static int factorial(int n) {   // ← yangi frame
        if (n <= 1) return 1;
        return n * factorial(n - 1);       // ← yana yangi frame
    }
    // metod tugaganda frame o'chiriladi — GC kerak emas!

    // StackOverflowError — frame stack to'lib ketganda
    public static void infiniteRecursion() {
        infiniteRecursion(); // ← StackOverflowError
    }
    // -Xss512k  → thread stack hajmini belgilash
}
