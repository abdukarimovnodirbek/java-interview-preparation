package uz.abdukarimov.generic;

import java.util.ArrayList;
import java.util.List;

public class WhatIsGeneric {
    static void main() {
        // Genericssiz — xavfli
        List list = new ArrayList();
        list.add("Hello");
        //list.add(123);           // Integer ham qo'shildi!
        String withoutGeneric = (String) list.get(0); // Runtime: ClassCastException 💥
        System.out.println(withoutGeneric);

        // Generics bilan — xavfsiz
        List<String> safeList = new ArrayList<>();
        safeList.add("Hello");
        //safeList.add(123);       // Compile-time xato ✅
        String withGeneric = safeList.get(0); // Cast shart emas
        System.out.println(withGeneric);
    }
}
