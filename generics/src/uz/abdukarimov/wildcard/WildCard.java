package uz.abdukarimov.wildcard;

import java.util.ArrayList;
import java.util.List;

public class WildCard {
    static void main() {
        // ? — har qanday type bo'lishi mumkin
        List<?> anyList = new ArrayList<String>();
        //anyList = new ArrayList<Integer>();  // ham ishlaydi

        Object obj = anyList.get(0); // faqat Object sifatida o'qisa bo'ladi
        // anyList.add("something");  // ❌ yozib bo'lmaydi (type noma'lum)
    }
}
