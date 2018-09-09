package com.wechat.commons;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleLockFactory {
	private static ConcurrentHashMap<String, WeakLockRef<String, ReentrantLock>> lockMap = new ConcurrentHashMap<>();
    private static ReferenceQueue<ReentrantLock> queue = new ReferenceQueue<>();

    public static ReentrantLock getInstance(String key) {
        if (lockMap.size() > 500) {// 超过阈值清理
            clearEmptyRef();
        }
        WeakReference<ReentrantLock> lockRef = lockMap.get(key);
        ReentrantLock lock = (lockRef == null ? null : lockRef.get());
        while (lock == null) {
            lockMap.putIfAbsent(key, new WeakLockRef<>(new ReentrantLock(), queue, key));
            lockRef = lockMap.get(key);
            lock = (lockRef == null ? null : lockRef.get());
            if (lock != null) {
                return lock;
            }
            clearEmptyRef();
        }
        return lock;
    }

    @SuppressWarnings("unchecked")
    private static void clearEmptyRef() {
        Reference<? extends ReentrantLock> ref;
        while ((ref = queue.poll()) != null) {
            WeakLockRef<String, ? extends ReentrantLock> weakLockRef = (WeakLockRef<String, ? extends ReentrantLock>) ref;
            lockMap.remove(weakLockRef.key);
        }
    }

    private static final class WeakLockRef<T, K> extends WeakReference<K> {
        final T key;

        private WeakLockRef(K referent, ReferenceQueue<? super K> q, T key) {
            super(referent, q);
            this.key = key;
        }
    }

}
