package com.sik.ivb.api;

import java.util.Comparator;

public class CalendarEventComparator implements Comparator<CalendarEvent> {

	@Override
	public int compare(final CalendarEvent e1, final CalendarEvent e2) {
		int retVal = e1.getStartDate().compareTo(e2.getStartDate());
		if (retVal == 0) {
			retVal = e1.getEndDate().compareTo(e2.getEndDate());
		}
		return retVal;
	}

}
