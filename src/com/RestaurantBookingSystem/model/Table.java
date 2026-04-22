package com.RestaurantBookingSystem.model;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Table {
    private final String tableId;
    private final int capacity;
    private final Set<TimeSlot> bookedSlots = ConcurrentHashMap.newKeySet();

    public Table(String tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
    }

    public String getTableId() { return tableId; }
    public int getCapacity() { return capacity; }

    public boolean isAvailable(TimeSlot slot) {
        return !bookedSlots.contains(slot);
    }

    public boolean bookSlot(TimeSlot slot) {
        return bookedSlots.add(slot);
    }

    public void releaseSlot(TimeSlot slot) {
        bookedSlots.remove(slot);
    }

    @Override
    public String toString() {
        return "Table{id=" + tableId + ", capacity=" + capacity + "}";
    }
}
