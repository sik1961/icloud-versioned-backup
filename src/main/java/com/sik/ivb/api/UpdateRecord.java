package com.sik.ivb.api;

import java.time.LocalDateTime;

/**
 * @author sik
 *
 */

public class UpdateRecord {
    private CalendarEvent event;
    private LocalDateTime lastUpdated;
    public UpdateRecord() {
        super();
        this.lastUpdated = LocalDateTime.ofEpochSecond(0L, 0, null);
    }
    public UpdateRecord(final CalendarEvent lastUpdatedBy, final LocalDateTime lastUpdated) {
        super();
        this.event = lastUpdatedBy;
        this.lastUpdated = lastUpdated;
    }
    public CalendarEvent getEvent() {
        return this.event;
    }
    public void setEvent(final CalendarEvent event) {
        this.event = event;
    }
    public LocalDateTime getLastUpdated() {
        return this.lastUpdated;
    }
    public void setLastUpdated(final LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
