package uz.abdukarimov.racecondition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {

    public static class Cache<K, V> {
        private final Map<K, V> map = new HashMap<>();
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final Lock readLock = rwLock.readLock();
        private final Lock writeLock = rwLock.writeLock();

        // Ko'p thread bir vaqtda O'QISHI mumkin
        public V get(K key) {
            readLock.lock();
            try {
                return map.get(key);   // parallel o'qish OK!
            } finally {
                readLock.unlock();
            }
        }

        // Faqat bitta thread YOZISHI mumkin (o'qish bloklanadi)
        public void put(K key, V value) {
            writeLock.lock();
            try {
                map.put(key, value);   // eksklyuziv yozish
            } finally {
                writeLock.unlock();
            }
        }

        public void remove(K key) {
            writeLock.lock();
            try {
                map.remove(key);
            } finally {
                writeLock.unlock();
            }
        }

        public int size() {
            readLock.lock();
            try {
                return map.size();
            } finally {
                readLock.unlock();
            }
        }
    }
}

//  O'quvchilar (read):     Yozuvchi (write):
//  ┌─────────────────┐     ┌─────────────────┐
//  │ T1 o'qiydi  ✅  │     │                 │
//  │ T2 o'qiydi  ✅  │  vs │ T1 yozadi   ✅  │
//  │ T3 o'qiydi  ✅  │     │ T2 BLOCKED  ⛔  │
//  │ bir vaqtda      │     │ T3 BLOCKED  ⛔  │
//  └─────────────────┘     └─────────────────┘
//  Parallel ruxsat           Eksklyuziv huquq