package com.MessageQueue.broker.interfaces;

import com.MessageQueue.consumer.Consumer;
import com.MessageQueue.model.Message;

public interface Broker {
    void registerConsumer(Consumer consumer);
    void unregisterConsumer(String consumerId);
    void publish(Message msg) throws InterruptedException;
    boolean tryPublish(Message msg);
    void start();
    void stop();
}
