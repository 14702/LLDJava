package com.RestaurantBookingSystem.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TimeSlot {
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeSlot(LocalDate date, LocalTime startTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = startTime.plusHours(1);
    }

    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) return false;
        TimeSlot that = (TimeSlot) o;
        return date.equals(that.date) && startTime.equals(that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, startTime);
    }

    @Override
    public String toString() {
        return date + " [" + startTime + " - " + endTime + "]";
    }
}
