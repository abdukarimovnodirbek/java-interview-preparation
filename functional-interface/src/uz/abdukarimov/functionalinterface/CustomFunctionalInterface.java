package uz.abdukarimov.functionalinterface;

@FunctionalInterface
interface CustomFunctionalInterface {
    int apply(int x, int y);
    // default metodlar bo'lishi mumkin — hisoblanmaydi
}