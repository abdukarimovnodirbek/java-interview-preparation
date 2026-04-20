package uz.abdukarimov.stack;

import java.util.Iterator;

public class Main {
    static void main(String[] args) {
        // Integer Stack
        GenericStack<Integer> stack = new GenericStack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);

        System.out.println(stack);          // Stack[10, 20, 30] ← top
        System.out.println(stack.peek());   // 30
        System.out.println(stack.pop());    // 30
        System.out.println(stack.size());   // 2
        System.out.println(stack.search(10)); // 2 (tepadan 2-o'rinda)

        // String Stack
        GenericStack<String> strStack = new GenericStack<>();
        strStack.push("Java");
        strStack.push("Generics");
        strStack.push("Rules");

        // Iterator bilan
        Iterator<String> it = strStack.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " "); // Rules Generics Java
        }

        // Bracket balansini tekshirish (amaliy misol)
        System.out.println(isBalanced("({[]})"));  // true
        System.out.println(isBalanced("({[})"));   // false
    }

    // Stack bilan amaliy misol — bracket tekshirish:
    public static boolean isBalanced(String expr) {
        GenericStack<Character> stack = new GenericStack<>();
        for (char ch : expr.toCharArray()) {
            if ("({[".indexOf(ch) >= 0) {
                stack.push(ch);
            } else if (")}]".indexOf(ch) >= 0) {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if ((ch == ')' && top != '(') ||
                        (ch == '}' && top != '{') ||
                        (ch == ']' && top != '[')) return false;
            }
        }
        return stack.isEmpty();
    }
}
