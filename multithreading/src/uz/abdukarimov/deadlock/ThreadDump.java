package uz.abdukarimov.deadlock;

public class ThreadDump {
   /*
   jstack va VisualVM chiqaradigan thread dump ni qanday o'qish kerakligini ko'rib chiqamiz.
   Quyidagi interaktiv widget real deadlock dump ni bosqichma-bosqich tahlil qilishga yordam beradi:
   Found one Java-level deadlock:
            =============================
            "Thread-1":
    waiting to lock monitor 0x00000007d7c3e7f8 (object of type java.lang.Object LOCK_B),
    which is held by "Thread-2"
            "Thread-2":
    waiting to lock monitor 0x00000007d7c3e7b0 (object of type java.lang.Object LOCK_A),
    which is held by "Thread-1"

    Java stack information for the threads listed above:
            ===================================================
            "Thread-1"
    java.lang.Thread.State: BLOCKED (on object monitor)
    at DeadlockDemo.lambda$0(DeadlockDemo.java:18)
  - waiting to lock <0x00000007d7c3e7f8> (a java.lang.Object) ← LOCK_B
  - locked <0x00000007d7c3e7b0> (a java.lang.Object) ← LOCK_A ushlab turibdi
    at java.lang.Thread.run(Thread.java:833)

            "Thread-2"
    java.lang.Thread.State: BLOCKED (on object monitor)
    at DeadlockDemo.lambda$1(DeadlockDemo.java:32)
  - waiting to lock <0x00000007d7c3e7b0> (a java.lang.Object) ← LOCK_A
  - locked <0x00000007d7c3e7f8> (a java.lang.Object) ← LOCK_B ushlab turibdi
    at java.lang.Thread.run(Thread.java:833)

            "main"
    java.lang.Thread.State: RUNNABLE
    at DeadlockDemo.main(DeadlockDemo.java:42)

    "Finalizer"
    java.lang.Thread.State: WAITING
    at java.lang.Object.wait(Native Method)

    Found 1 deadlock.

    Deadlock zanjiri

    Thread-1 → LOCK_B kutayapti
    LOCK_B → Thread-2 ushlab turibdi
    Thread-2 → LOCK_A kutayapti
    LOCK_A → Thread-1 ushlab turibdi

    ↻ Dumaloq kutish!

    O'qish qoidalari

    "waiting to lock" → kutayotgan lock
    "locked" → ushlab turgan lock
    BLOCKED → lock kutmoqda
    WAITING → wait() da
    RUNNABLE → ishlayapti

    # ── jstack — command line ──────────────────────────────────────

# 1. Java process ID ni topish
jps -l
# Chiqish:
# 12345 DeadlockDemo
# 67890 org.gradle.launcher.daemon.bootstrap.GradleDaemon

# 2. Thread dump olish
jstack 12345
jstack 12345 > thread_dump.txt  # faylga saqlash

# 3. Deadlock topish (avtomatik)
jstack -l 12345          # lock ma'lumotlari bilan
jstack -F 12345          # thread javob bermasa (force)

# 4. Bir necha marta dump olib taqqoslash (muammo qayerda)
for i in 1 2 3; do
  jstack 12345 > dump_$i.txt
  sleep 5
done

# ── jcmd — zamonaviy alternativa ──────────────────────────────
jcmd 12345 Thread.print
jcmd 12345 Thread.print > dump.txt

# ── VisualVM — GUI ─────────────────────────────────────────────
# 1. VisualVM ni ishga tushirish:
#    jvisualvm  (JDK bilan birga keladi)
#    yoki https://visualvm.github.io/ dan yuklab olish

# 2. Local Applications → DeadlockDemo → Threads tab
# 3. "Detect Deadlock" tugmasi
# 4. Thread holatlari rangi bo'yicha:
#    Yashil  = RUNNABLE
#    Sariq   = WAITING / TIMED_WAITING
#    Qizil   = BLOCKED  ← deadlock ko'rsatkichi!
    */
}
