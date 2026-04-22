package com.MessageQueue.queue.interfaces;

import com.MessageQueue.model.Message;

public interface Queue {
    void put(Message msg) throws InterruptedException;
    boolean offer(Message msg);
    Message take() throws InterruptedException;
    int size();
    int capacity();
}
