package com.MessageQueue.filter;

import com.MessageQueue.model.Message;

/**
 * Strategy pattern: consumers provide a filter to match messages of interest.
 */
@FunctionalInterface
public interface MessageFilter {

    boolean matches(Message message);

    static MessageFilter acceptAll() {
        return msg -> true;
    }

    static MessageFilter keyEquals(String key, Object value) {
        return msg -> value.equals(msg.getPayload().get(key));
    }

    static MessageFilter keyContains(String key, String substring) {
        return msg -> {
            Object v = msg.getPayload().get(key);
            return v instanceof String && ((String) v).contains(substring);
        };
    }
}
