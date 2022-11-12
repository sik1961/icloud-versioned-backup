/**
 * @author sik
 */
package com.sik.ivb.html;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;

import com.sik.ivb.api.CalendarEvent;
import com.sik.ivb.events.EventManager;
import com.sik.ivb.events.EventUtility;
import com.sik.ivb.utils.M4DateUtils;

public class NewsPageBuilder {
	private static final Logger LOG = LogManager.getLogger(NewsPageBuilder.class);

	private static final DateTimeFormatter NEWS_GIG_DF = DateTimeFormatter.ofPattern("EEE d MMMM ha");
	private static final String NEWLINE = "\n";
		
	//private static final String FIXED_NEWS_1 = "<li>Pictures from our gig at The Hillsborough Hotel are on the gallery page - click <a href=\"gallery.htm\">here</a>";
	private static final String FIXED_NEWS_2 = "<li>To book <b2>Mark IV</b2> eMail us at <a href=\"mailto:markiv.band@gmail.com\">markiv.band@gmail.com</a>";
	//private static final String FIXED_NEWS_3 = "<li>We're now registered on	<b2><a href=\"http://www.gigz4u.co.uk/\">GIGZ4U</a></b2> - "
	//		+ "Use it to find our gigs and other live music in the Sheffield area.";
	private static final String FIXED_NEWS_4 = "<li>Listen to our Demo CD \"Drivin' West\" on SoundCloud By clicking "
			+ "<a href=\"https://soundcloud.com/markiv-1/sets/drivin-west\" target=\"-blank\">here</a>";
	
	private EventManager eventMgr;
	private HtmlManager htmlMgr;
	private M4DateUtils du;
	private EventUtility eu = new EventUtility();
	

	public NewsPageBuilder(final EventManager eventMgr) {
		super();

		this.htmlMgr = new HtmlManager();
		this.du = new M4DateUtils();
		this.eventMgr = eventMgr;
	}

	public String buildNews(final String header, final String trailer)
			throws IOException {

		final StringBuilder out = new StringBuilder();

		if (header != null) {
			out.append(this.htmlMgr.readFileAsString(header));
		}
		out.append(this.newsBodyBuilder());
		if (trailer != null) {
			out.append(this.htmlMgr.readFileAsString(trailer));
		}

		return out.toString();
	}

	public String newsBodyBuilder() {
		List<CalendarEvent> gigs = eventMgr.getConfirmedGigs();

		final StringBuilder newsHtml = new StringBuilder();
		// set up title
		newsHtml.append(HtmlSnippets.NEWS_HEAD_FMT1)
			.append(HtmlSnippets.NEWS_HEAD_FMT2)
			.append(HtmlSnippets.NEWS_HEAD_FMT3)
			.append(HtmlSnippets.NEWS_HEAD_FMT4)
			.append(HtmlSnippets.NEWS_HEAD_FMT5)
			.append(HtmlSnippets.NEWS_HEAD_FMT6)
			.append(HtmlSnippets.NEWS_HEAD_FMT7);
		
		CalendarEvent nextPublicGig = this.getNextPublicGig(gigs);
		if (nextPublicGig != null) {
			LOG.info("Next gig: " + nextPublicGig.toString());
			newsHtml.append(String.format(HtmlSnippets.NEWS_FORMAT, 
					this.du.adjustForDaylightSaving(nextPublicGig.getStartDate()).format(NEWS_GIG_DF), 
					HtmlSnippets.cleanForHtml(nextPublicGig.getLocation())));
		}
		
		for (String newsItem: this.getFixedNewsItems()) {
			LOG.info("News item: " + newsItem);
			newsHtml.append(HtmlSnippets.cleanForHtml(newsItem + HtmlSnippets.HTML_BREAK + HtmlSnippets.HTML_BREAK + NEWLINE));
		}
				 
		newsHtml.append(String.format(HtmlSnippets.LAST_UPDATE_FORMAT,	new Date()));
		
		newsHtml.append(HtmlSnippets.NEWS_TAIL_FMT8 + NEWLINE);
		
		return newsHtml.toString();
	}

	/**
	 * @param gigs
	 * @return
	 */
	private CalendarEvent getNextPublicGig(List<CalendarEvent> gigs) {
		if (gigs.size() > 0) {
			for (final CalendarEvent e : gigs) {
				if (eu.isGig(e) && !eu.isEventPrivate(e)) {
					return e;
				}
			}
		}
		return null;
	}

	private List<String> getFixedNewsItems() {
		List<String> fixedNewsItems = new ArrayList<String>();
		fixedNewsItems.add(FIXED_NEWS_2);
		fixedNewsItems.add(FIXED_NEWS_4);
		return fixedNewsItems; 
	}
}