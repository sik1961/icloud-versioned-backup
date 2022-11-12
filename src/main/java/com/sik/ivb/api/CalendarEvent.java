package com.sik.ivb.api;
/**
 * @author sik
 */
//import org.joda.time.LocalDateTime;

import java.time.LocalDateTime;

import com.sik.ivb.exception.MarkIVException;

public class CalendarEvent {
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String summary;
	private String location;
	private String notes;
	private LocalDateTime lastUpdated;
	private String lastUpdatedBy;
	private EventType eventType;
	private Boolean eventPrivate;
	private Boolean confirmed;
	
	    
	public CalendarEvent() {}
	
    public LocalDateTime getStartDate() {
		return this.startDate;
	}

	public CalendarEvent withStartDate(final LocalDateTime startDate) {
		this.startDate = startDate;
		return this;
	}

	public LocalDateTime getEndDate() {
		return this.endDate;
	}

	public CalendarEvent withEndDate(final LocalDateTime endDate) {
		this.endDate = endDate;
		return this;
	}

	public String getSummary() {
		return this.summary;
	}

	public CalendarEvent withSummary(final String remarks) {
		this.summary = remarks;
		return this;
	}

	public String getLocation() {
		return this.location;
	}

	public CalendarEvent withLocation(final String location) {
		this.location = location;
		return this;
	}

	public String getNotes() {
        return this.notes;
    }

    public CalendarEvent withNotes(final String notes) {
        this.notes = notes;
        return this;
    }

	public LocalDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public CalendarEvent withLastUpdated(final LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public CalendarEvent withLastUpdatedBy(final String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public CalendarEvent withEventType(final EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public Boolean isEventPrivate() {
        return this.eventPrivate;
    }

    public CalendarEvent withEventPrivate(final Boolean eventPrivate) {
        this.eventPrivate = eventPrivate;
        return this;
    }
    
    public Boolean isConfirmed() {
        return this.confirmed;
    }

    public CalendarEvent withConfirmed(final Boolean confirmed) {
        this.confirmed = confirmed;
        return this;
    }
    
    public CalendarEvent validate() {
		if (this.startDate ==  null ||
				this.endDate == null ||
				this.summary == null) {
			throw new MarkIVException("Invalid CalendarEvent! Minimum start date, end date & summary required");
		}
		return this;
	}
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
		result = prime * result
				+ ((this.location == null) ? 0 : this.location.hashCode());
		result = prime * result + ((this.summary == null) ? 0 : this.summary.hashCode());
		result = prime * result
				+ ((this.startDate == null) ? 0 : this.startDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final CalendarEvent other = (CalendarEvent) obj;
		if (this.endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!this.endDate.equals(other.endDate))
			return false;
		if (this.location == null) {
			if (other.location != null)
				return false;
		} else if (!this.location.equals(other.location))
			return false;
		if (this.summary == null) {
			if (other.summary != null)
				return false;
		} else if (!this.summary.equals(other.summary))
			return false;
		if (this.startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!this.startDate.equals(other.startDate))
			return false;
		return true;
	}

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("CalendarEvent [startDate=");
        builder.append(this.startDate);
        builder.append(", endDate=");
        builder.append(this.endDate);
        builder.append(", remarks=");
        builder.append(this.summary);
        builder.append(", notes=");
        builder.append(this.notes);
        builder.append(", location=");
        builder.append(this.location);
        builder.append(", lastUpdated=");
        builder.append(this.lastUpdated);
        builder.append(", eventType=");
        builder.append(this.eventType);
        builder.append(", eventPrivate=");
        builder.append(this.eventPrivate);
        builder.append(", confirmed=");
        builder.append(this.confirmed);
        builder.append("]");
        return builder.toString();
    }
}
