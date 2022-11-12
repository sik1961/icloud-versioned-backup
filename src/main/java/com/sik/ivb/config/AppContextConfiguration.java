package com.sik.ivb.config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.sik.ivb.google.calendar.MarkIVCalendarFeed;

@Configuration
@PropertySource("file:${env.markiv.properties.location}/markiv.properties")
public class AppContextConfiguration {
	private static final Logger LOG = LogManager.getLogger(AppContextConfiguration.class);
	
	@Bean
	MarkIVCalendarFeed getFeed() {
		LOG.info("Initialising Calendar feed....");
		return new MarkIVCalendarFeed();
	}

}
