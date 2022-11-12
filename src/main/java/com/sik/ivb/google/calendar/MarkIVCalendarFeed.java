package com.sik.ivb.google.calendar;
/**
 * @author sik
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.sik.ivb.exception.MarkIVException;
import com.sik.ivb.utils.PropsUtils;
 
public class MarkIVCalendarFeed {
	private static final Logger LOG = LogManager.getLogger(MarkIVCalendarFeed.class);
	private String feedUrl;
	private static final String PROPS_FILE = "/home/sik/markiv/conf/markiv.properties";
	
    public InputStream getFeed() {
    	final PropsUtils pr = new PropsUtils();

		Properties props = pr.readProperties(PROPS_FILE);
    	this.feedUrl = props.getProperty("FeedUrl");
    	
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("Getting Calendar Feed from: " + feedUrl);
    	}
    	HttpTransport httpTransport = new NetHttpTransport();
    	HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
    	//String contentType = "UTF-8";
    	HttpRequest httpRequest;
    	HttpResponse resp = null;
		try {
			URL url = new URL(feedUrl);
			httpRequest = requestFactory.buildGetRequest(new GenericUrl(url));
			resp = httpRequest.execute();
			if (resp.getStatusCode() != 200) {
				throw new MarkIVException("Invalid HTTP Status: " + resp.getStatusCode());
			}
		} catch (IOException e) {
			throw new MarkIVException("I/O Error on request.execute(): " + e.getMessage());
		}
		try {
			LOG.info("Calendar Feed returned HTTP Status: " + resp.getStatusCode());
			return resp.getContent();
		} catch (IOException e) {
			throw new MarkIVException("I/O Error on getContent(): " + e.getMessage());
		}
    }
}