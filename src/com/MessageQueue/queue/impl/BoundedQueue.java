package com.MessageQueue.queue.impl;

import com.MessageQueue.model.Message;
import com.MessageQueue.queue.interfaces.Queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue implements Queue {
    private final Message[] buffer;
    private int head, tail, size;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BoundedQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.buffer = new Message[capacity];
    }

    public void put(Message msg) throws InterruptedException {
        lock.lock();
        try {
            while (size == buffer.length) notFull.await();
            buffer[tail] = msg;
            tail = (tail + 1) % buffer.length;
            size++;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean offer(Message msg) {
        lock.lock();
        try {
            if (size == buffer.length) return false;
            buffer[tail] = msg;
            tail = (tail + 1) % buffer.length;
            size++;
            notEmpty.signalAll();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public Message take() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0) notEmpty.await();
            Message msg = buffer[head];
            buffer[head] = null;
            head = (head + 1) % buffer.length;
            size--;
            notFull.signalAll();
            return msg;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try { return size; } finally { lock.unlock(); }
    }

    public int capacity() { return buffer.length; }
}
