package com.MessageQueue.consumer;

import com.MessageQueue.model.Message;

@FunctionalInterface
public interface MessageCallback {
    void onMessage(Message message) throws Exception;
}
