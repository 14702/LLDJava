package com.MessageQueue.consumer;

import com.MessageQueue.filter.MessageFilter;

import java.util.Collections;
import java.util.List;

public class Consumer {
    private final String id;
    private final MessageCallback callback;
    private final MessageFilter filter;
    private final List<String> dependencies; // consumer IDs that must process first

    public Consumer(String id, MessageCallback callback, MessageFilter filter, List<String> dependencies) {
        if (id == null || callback == null) throw new IllegalArgumentException("id and callback required");
        this.id = id;
        this.callback = callback;
        this.filter = filter != null ? filter : MessageFilter.acceptAll();
        this.dependencies = dependencies != null ? dependencies : Collections.emptyList();
    }

    public Consumer(String id, MessageCallback callback, MessageFilter filter) {
        this(id, callback, filter, null);
    }

    public Consumer(String id, MessageCallback callback) {
        this(id, callback, null, null);
    }

    public String getId() { return id; }
    public MessageCallback getCallback() { return callback; }
    public MessageFilter getFilter() { return filter; }
    public List<String> getDependencies() { return dependencies; }
}
