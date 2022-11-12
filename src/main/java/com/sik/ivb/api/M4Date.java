package com.sik.ivb.api;

import java.time.LocalDateTime;

/**
 * @author sik
 *
 */
public class M4Date {
	
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public M4Date() {
		LocalDateTime now = LocalDateTime.now();
		this.startTime = now
				.withHour(0)
				.withMinute(0)
				.withSecond(0)
				.withNano(0);
		this.endTime = now
				.withHour(23)
				.withMinute(59)
				.withSecond(59)
				.withNano(999999);
	}
	
	public M4Date(LocalDateTime dateTime) {
		this.startTime = dateTime
				.withHour(0)
				.withMinute(0)
				.withSecond(0)
				.withNano(0);
		this.endTime = dateTime
				.withHour(23)
				.withMinute(59)
				.withSecond(59)
				.withNano(999999);
	}
	
	/**
	 * @return the startTime
	 */
	public LocalDateTime getStartTime() {
		return this.startTime;
	}
	/**
	 * @return the endTime
	 */
	public LocalDateTime getEndTime() {
		return this.endTime;
	}
	
	public void rollDate(int i) {
		this.startTime = this.startTime.plusDays(i);
		this.endTime = this.endTime.plusDays(i);
	}
	
	/**
	 * @return
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("M4Date [startTime=");
		builder.append(this.startTime);
		builder.append(", endTime=");
		builder.append(this.endTime);
		builder.append("]");
		return builder.toString();
	}
}
