package com.sik.ivb;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author sik
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.sik.ivb.api.UpdateRecord;
import com.sik.ivb.events.EventManager;
import com.sik.ivb.exception.MarkIVException;
import com.sik.ivb.google.calendar.MarkIVCalendarFeed;
import com.sik.ivb.utils.MarkIVHelper;

@SpringBootApplication
@EnableScheduling
public class Application {

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	private static final String DAYTIME = "0 0 7-23 * * *"; // hourly 7am-11pm
	private static final String END_OF_DAY = "0 30 23 * * *"; // daily @ 11:30pm
	private static final Logger LOG = LogManager.getLogger(Application.class);

	MarkIVHelper m4h;
	private EventManager em;
	private MarkIVCalendarFeed feed;
	private LocalDateTime webLastUpdateTime;

	public Application() {
		LOG.info("Mark IV Mgt - Initialised");
		this.feed = new MarkIVCalendarFeed();
		this.em = new EventManager(this.feed.getFeed());
		this.m4h = new MarkIVHelper(em);
		this.webLastUpdateTime = LocalDateTime.ofEpochSecond(0, 0, null);
	}

	public static void main(String[] args) throws Exception {
		new SpringApplication(Application.class).run(args);
	}
	
	@Scheduled(cron = DAYTIME)
	public void m4Update() throws MarkIVException {
		update(false);
	}

	@Scheduled(cron = END_OF_DAY)
	public void m4DailyUpdate() throws MarkIVException {
		update(true);
	}

	protected void update(final boolean forceUpdate) {
		LOG.info("Mark IV Mgt - Checking calendar....");

		em.updateFromFeed(new MarkIVCalendarFeed().getFeed());

		UpdateRecord lu = em.getLatestUpdate();

		LOG.info("Cal updated: " + lu.getLastUpdated().format(dtf) + 
				" - Web updated: " + this.webLastUpdateTime.format(dtf));

		if (forceUpdate || lu.getLastUpdated().isAfter(this.webLastUpdateTime)) {

			if (!forceUpdate) {
				LOG.info("Last update: " + lu.getLastUpdated().format(dtf)
						+ " (" + lu.getEvent().getEventType() + ":"
						+ lu.getEvent().getSummary() + ")");
			}

			LOG.info("Mark IV Mgt - Building Gigs Page");
			m4h.buildGigsPage();

			LOG.info("Mark IV Mgt - Building Availability Page");
			m4h.buildAvailPage();
			
			LOG.info("Mark IV Mgt - Availablity Stats");
			m4h.doAvailabilityStats();
			
			LOG.info("Mark IV Mgt - Building News Page");
			m4h.buildNewsPage();

			LOG.info("Mark IV Mgt - Building Gallery Page");
			m4h.buildGalleryPage();

			LOG.info("Mark IV Mgt - Uploading");
			try {
				if (m4h.uploadFiles()) {
					this.webLastUpdateTime = LocalDateTime.now();
					LOG.info("Mark IV Mgt - Last update time set to: "
							+ this.webLastUpdateTime.toString());
				}
				
			} catch (MarkIVException e) {
				throw new MarkIVException("Caught Exception: ", e);
			}
			
			LOG.info("Mark IV Mgt - Completed");
		} else {
			LOG.info("Mark IV Mgt - Skipped - no calendar updates");
		}
	}
}
