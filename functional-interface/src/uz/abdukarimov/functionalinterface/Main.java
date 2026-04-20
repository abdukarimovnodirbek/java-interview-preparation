package uz.abdukarimov.functionalinterface;

public class Main {
    static void main() {
        CustomFunctionalInterface sum = (a, b) -> a + b;
        System.out.println(sum.apply(3, 4)); // 7
    }
}
