package uz.abdukarimov.memoryleak;

// Eclipse MAT (Memory Analyzer Tool) heap dump tahlili uchun eng kuchli vosita.
public class MAT {
/*
# ── Heap Dump olish ──────────────────────────────────────────────

# 1. Avtomatik (OOM da):
    java -XX:+HeapDumpOnOutOfMemoryError \
            -XX:HeapDumpPath=/tmp/heap_$(date +%Y%m%d_%H%M%S).hprof \
            -jar app.jar

# 2. Qo'lda (ishlab turgan dastur):
    jmap -dump:format=b,live,file=/tmp/heap.hprof <pid>

# 3. jcmd (zamonaviy usul):
    jcmd <pid> GC.heap_dump /tmp/heap.hprof

# 4. VisualVM GUI orqali:
            #    Applications → [dastur] → Monitor → "Heap Dump" tugmasi

# ── MAT ishga tushirish ──────────────────────────────────────────
            # Eclipse MAT yuklab olish:
            # https://www.eclipse.org/mat/downloads.php

            # Heap dump ochish:
            # File → Open Heap Dump → heap.hprof tanlash

# ── MAT asosiy ko'rinishlari ─────────────────────────────────────
            # 1. Overview → Leak suspects (avtomatik topish)
# 2. Histogram → Klass bo'yicha instance/memory
            # 3. Dominator Tree → eng ko'p xotira ushlab turgan ob'ektlar
# 4. OQL — Object Query Language

# ── OQL misollar ────────────────────────────────────────────────
            # Barcha 1MB dan katta ob'ektlar:
    SELECT * FROM java.lang.Object o WHERE o.@retainedHeapSize > 1048576

            # Barcha tirik String lar (qisqacha):
    SELECT s.toString() FROM java.lang.String s

# Barcha HashMap 1000 dan ko'p element bilan:
    SELECT m, m.size FROM java.util.HashMap m WHERE m.size > 1000*/
}
