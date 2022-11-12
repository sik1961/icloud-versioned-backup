/**
 * @author sik
 */
package com.sik.ivb.html;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.joda.time.LocalDateTime;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;

import com.sik.ivb.api.CalendarEvent;
import com.sik.ivb.api.EventType;
import com.sik.ivb.api.M4Date;
import com.sik.ivb.api.UpdateRecord;
import com.sik.ivb.events.EventManager;
import com.sik.ivb.utils.M4DateUtils;

public class GigsAvailabilityBuilder {
	private static final Logger LOG = LogManager.getLogger(GigsAvailabilityBuilder.class);

	private static final DateTimeFormatter GIG_DF = DateTimeFormatter.ofPattern("EEE d MMMM ha");
	private static final DateTimeFormatter AVL_DF = DateTimeFormatter.ofPattern("EEE d MMM yyyy").withLocale(Locale.UK);

	private static final String GIG = "gig";
	private static final int GIG_DAYS_AHEAD = 183;
	private static final int AVL_DAYS_AHEAD = 400;
	private static final String PRIVATE_PARTY = "Private Party";
	private static final String SUN = "Sun";
	private static final String AVAILABLE = "Available";
	private static final String EMPTY_STRING = "";
	private static final String NEWLINE = "\n";
	private static final String RECENT_ASTER = " *";

	private EventManager eventMgr;
	private HtmlManager htmlMgr;
	private M4DateUtils du;

	public GigsAvailabilityBuilder(final EventManager eventMgr) {
		super();

		this.htmlMgr = new HtmlManager();
		this.du = new M4DateUtils();
		this.eventMgr = eventMgr;
	}

	public String buildGigs(final String header, final String trailer)
			throws IOException {

		final StringBuilder out = new StringBuilder();

		if (header != null) {
			out.append(this.htmlMgr.readFileAsString(header));
		}
		
		out.append(this.gigsBodyBuilder());
		
		if (trailer != null) {
			out.append(this.htmlMgr.readFileAsString(trailer));
		}

		return out.toString();
	}

	public String gigsBodyBuilder() {

		List<CalendarEvent> gigs = eventMgr.getConfirmedGigs();

		final StringBuilder gigsHtml = new StringBuilder();
		// set up title
		gigsHtml.append(HtmlSnippets.GIG_HEAD_FMT1)
			.append(HtmlSnippets.GIG_HEAD_FMT2)
			.append(HtmlSnippets.GIG_HEAD_FMT3)
			.append(HtmlSnippets.GIG_HEAD_FMT4)
			.append(HtmlSnippets.GIG_HEAD_FMT5)
			.append(HtmlSnippets.GIG_HEAD_FMT6);
		StringBuilder location = null;
		if (gigs.size() > 0) {
			for (final CalendarEvent e : gigs) {
				location = new StringBuilder();
				LOG.info("Confirmed gig: " + e.getStartDate().toString() + " : " + e.getLocation());
				if (e.getStartDate()
						.isBefore(this.getDateHence(GIG_DAYS_AHEAD))) {
					if (isGig(e)) {
						if (e.isEventPrivate()) {
							location.append(PRIVATE_PARTY);
						} else {
							if (e.getLocation() != null) {
								location.append(HtmlSnippets.cleanForHtml(e.getLocation()));
							} else {
								location.append(HtmlSnippets.cleanForHtml(e.getSummary()));
							}
							if (!e.getNotes().isEmpty()) {
								location.append(HtmlSnippets.HTML_BREAK + HtmlSnippets.OPN_BKT);
								location.append(HtmlSnippets.cleanForHtml(e.getNotes()));
								location.append(HtmlSnippets.CLS_BKT);
							}
						}
						if (location != null && location.length() > 0) {
							gigsHtml.append(String.format(HtmlSnippets.GIG1_FORMAT,
											this.du.adjustForDaylightSaving(e.getStartDate()).format(GIG_DF), 
											getLocName(location)));
							gigsHtml.append(NEWLINE);
							gigsHtml.append(String.format(HtmlSnippets.GIG2_FORMAT,
											getLocAddr(location)));
							gigsHtml.append(NEWLINE);
						} else {
							LOG.warn("Location missing for event: " + e);
						}
					}
				}
			}
		} else {
			gigsHtml.append(HtmlSnippets.NO_GIGS + NEWLINE);
		}
		gigsHtml.append(HtmlSnippets.TR_END + HtmlSnippets.TABLE_END + NEWLINE);
		gigsHtml.append(String.format(HtmlSnippets.LAST_UPD_FMT, new Date()));
		LOG.info(gigs.size() + " Gig(s) found.");
		return gigsHtml.toString();
	}

	/**
	 * @param location
	 * @return
	 */
	private String getLocAddr(StringBuilder location) {
		return location.toString().indexOf(",") == -1 
			? ""
			: location.toString().substring(location.toString().indexOf(",") + 1);
	}

	/**
	 * @param location
	 * @return
	 */
	private String getLocName(StringBuilder location) {
		return location.toString().indexOf(",") == -1
			? location.toString()
			: location.toString().split(",")[0];
	}

	/**
	 * @param e
	 * @return
	 */
	private boolean isGig(CalendarEvent e) {
		return e.getSummary().toLowerCase().contains(GIG);
	}

	public String buildAvail(final String header, final String trailer)
			throws IOException {
		final StringBuilder out = new StringBuilder();

		if (header != null) {
			out.append(this.htmlMgr.readFileAsString(header));
		}
		out.append(this.availBodyBuilder());
		if (trailer != null) {
			out.append(this.htmlMgr.readFileAsString(trailer));
		}

		return out.toString();
	}

	public String availBodyBuilder() {

		final StringBuilder availHtml = new StringBuilder();

		final UpdateRecord ur = this.eventMgr.getLatestUpdate();

		availHtml.append(HtmlSnippets.TABLE);
		availHtml.append(HtmlSnippets.TR + HtmlSnippets.TD + NEWLINE);
		availHtml.append(HtmlSnippets.AVAIL_HEAD);
		availHtml.append(HtmlSnippets.TD_END + HtmlSnippets.TR_END + NEWLINE);
		availHtml.append(HtmlSnippets.TR + HtmlSnippets.TD + NEWLINE);
		availHtml.append(String.format(HtmlSnippets.N1 + HtmlSnippets.CAL_LAST_UPD_FMT + HtmlSnippets.N1_END + NEWLINE, ur
						.getLastUpdated().toLocalDate()));
		availHtml.append(String.format(HtmlSnippets.LAST_UPD_FMT, new Date()));
		availHtml.append(HtmlSnippets.TD_END + HtmlSnippets.TR_END + NEWLINE);
		availHtml.append(HtmlSnippets.TR + HtmlSnippets.TD + NEWLINE);
		availHtml.append(HtmlSnippets.N1 + NEWLINE);

		M4Date rolling = new M4Date();

		int availTotal = 0;
		boolean dateClear = true;
		StringBuilder remarks = null;
		for (int i = 0; i < AVL_DAYS_AHEAD; i++) {

			// LOG.info("Checking: " + rolling.getStartTime().toDate());

			final List<CalendarEvent> eventsOnDate = this.eventMgr
					.getEventsOn(rolling);
			remarks = new StringBuilder();
			dateClear = (eventsOnDate.size() == 0);

			if (dateClear) {
				availHtml.append(String.format(HtmlSnippets.FONT_COLOR_FMT, HtmlSnippets.GREEN));
				availTotal++;
			} else {
				this.debug(rolling.getStartTime().toLocalDate() + ": "
						+ eventsOnDate.size() + " event records found");
				for (CalendarEvent ce : eventsOnDate) {
					this.debug("\tce=" + ce);
				}
				boolean unavailable = false;
				for (final CalendarEvent e : eventsOnDate) {
					if (e.getEventType() == EventType.UNAVAILABILITY) {
						unavailable = true;
					}
					if (!remarks.toString().isEmpty()) {
						remarks.append(HtmlSnippets.SLASH);
					}
					remarks.append(e.getSummary());
					if (e.getLocation() != null && !e.getLocation().isEmpty()
							&& !e.isEventPrivate()) {
						remarks.append(HtmlSnippets.OPN_BKT);
						remarks.append(e.getLocation());
						remarks.append(HtmlSnippets.CLS_BKT);
					}
					if (this.isRecentlyUpdated(e.getLastUpdated())) {
						remarks.append(RECENT_ASTER);
					}
				}
				if (unavailable) {
					availHtml.append(String.format(HtmlSnippets.FONT_COLOR_FMT, HtmlSnippets.RED));
				} else {
					availHtml.append(String
							.format(HtmlSnippets.FONT_COLOR_FMT, HtmlSnippets.AMBER));
				}

			}
			availHtml.append(String.format(HtmlSnippets.AVAIL_FMT, 
					rolling.getStartTime().format(AVL_DF), 
					dateClear ? AVAILABLE:EMPTY_STRING, 
					remarks.toString()));
			availHtml.append(HtmlSnippets.FONT_END + NEWLINE);

			if (rolling.getStartTime().format(AVL_DF).startsWith(SUN)) {
				availHtml.append(HtmlSnippets.HTML_HR + NEWLINE);
			}
			rolling.rollDate(1);
		}
		availHtml.append(HtmlSnippets.TD_END + HtmlSnippets.TR_END + NEWLINE);
		availHtml.append(HtmlSnippets.TABLE_END);
		LOG.info("Availability: " + availTotal + "/" + AVL_DAYS_AHEAD
				+ " days available.");
		return availHtml.toString();
	}

	private LocalDateTime getDateHence(final int daysAhead) {
		return LocalDateTime.now().plusDays(daysAhead);
	}

	

	private Boolean isRecentlyUpdated(final LocalDateTime modDate) {
		return modDate.plusDays(7).isAfter(
				LocalDateTime.now());
	}
	
	private void debug(String msg) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(msg);
		}
	}
}