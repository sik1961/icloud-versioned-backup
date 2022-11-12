package com.sik.ivb.utils;
/**
 * @author sik
 *
 */
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sik.ivb.api.CalendarEvent;
import com.sik.ivb.api.EventType;
import com.sik.ivb.events.EventManager;
import com.sik.ivb.exception.MarkIVException;
import com.sik.ivb.ftp.MarkIVUploader;
import com.sik.ivb.html.GalleryManager;
import com.sik.ivb.html.GigsAvailabilityBuilder;
import com.sik.ivb.html.HtmlManager;
import com.sik.ivb.html.NewsPageBuilder;

public class MarkIVHelper {
	private static final Logger LOG = LogManager.getLogger(MarkIVHelper.class);
	private static final String PROPS_FILE = "/home/sik/markiv/conf/markiv.properties";
	private static final DecimalFormat DEC_FMT = new DecimalFormat("###.##");
	private static final double DAYS_IN_YEAR = 365.0D;
	private static final int HUNDRED = 100;

	private Properties props;
	private PropsUtils pu = new PropsUtils(); 
	private String localWebRoot;
	private String galleryDir;
	private String htmlHeaderFile;
	private String htmlTrailerFile;
	private String gigsHtmlFile;
	private String newsHtmlFile;
	private String availHtmlFile;
	private String gallHtmlFile;
	private GigsAvailabilityBuilder gigAvailBuilder;
	private NewsPageBuilder newsBuilder;
	private EventManager em;

	public MarkIVHelper(EventManager em) {
		this.em = em;
		this.gigAvailBuilder = new GigsAvailabilityBuilder(this.em);
		this.newsBuilder = new NewsPageBuilder(this.em);
		this.props = this.readProps();
		localWebRoot = props.getProperty("LocalWebRoot");
		galleryDir = props.getProperty("UploadDirs");
		htmlHeaderFile = props.getProperty("HtmlHeaderFile");
		htmlTrailerFile = props.getProperty("HtmlTrailerFile");
		gigsHtmlFile = props.getProperty("GigsHtmlFile");
		newsHtmlFile = props.getProperty("NewsHtmlFile");
		availHtmlFile = props.getProperty("AvailHtmlFile");
		gallHtmlFile = props.getProperty("GalleryHtmlFile");
	}

	public boolean uploadFiles() throws MarkIVException {
		final MarkIVUploader mul = new MarkIVUploader(props);

		final List<String> uploadFiles = new ArrayList<String>();
		for (final String f : Arrays.asList(props.getProperty("UploadFiles").split(","))) {
			uploadFiles.add(f);
		}

		return mul.upload(props.getProperty("ProjectName"), uploadFiles);
	}
	
	public void doAvailabilityStats() {
		Map<String,Integer> statMap = new TreeMap<>();
		for (CalendarEvent e: em.getByType(EventType.UNAVAILABILITY, System.currentTimeMillis(), false)) {
			LOG.debug(e.toString());
			statMap = updateStatMap(e.getSummary(), statMap);
		}
		for (String key: statMap.keySet()) {
			LOG.info("Unavailable: " + key.toUpperCase() + " " + statMap.get(key) + " days " + 
					 DEC_FMT.format((statMap.get(key)/DAYS_IN_YEAR)*HUNDRED) + "%");
		}
	}

	private Map<String,Integer> updateStatMap(String summary, Map<String, Integer> statMap) {
		String mapKey = summary.toLowerCase().replace("unavailable", "").trim();
		if (statMap.get(mapKey)==null) {
			statMap.put(mapKey, 1);
		} else {
			statMap.put(mapKey, statMap.get(mapKey) + 1);
		}
		return statMap;
	}

	public void buildAvailPage() {
		final HtmlManager htmlMan = new HtmlManager();
		try {
			htmlMan.writeHtmlFile(availHtmlFile,
					gigAvailBuilder.buildAvail(htmlHeaderFile, htmlTrailerFile));
		} catch (final IOException e) {
			throw new MarkIVException("Exception occurred:",e);
		}

	}

	public void buildGigsPage() {
		try {
			final HtmlManager htmlMan = new HtmlManager();
			htmlMan.writeHtmlFile(gigsHtmlFile,
					gigAvailBuilder.buildGigs(htmlHeaderFile, htmlTrailerFile));
		} catch (final IOException e) {
			throw new MarkIVException("Exception occurred:",e);
		}
	}
	
	public void buildNewsPage() {
		try {
			final HtmlManager htmlMan = new HtmlManager();
			htmlMan.writeHtmlFile(newsHtmlFile,
					newsBuilder.buildNews(htmlHeaderFile, htmlTrailerFile));
		} catch (final IOException e) {
			throw new MarkIVException("Exception occurred:",e);
		}
	}

	public void buildGalleryPage() {
		final GalleryManager galleryManager = new GalleryManager();
		final HtmlManager htmlMan = new HtmlManager();
		try {
			htmlMan.writeHtmlFile(
					gallHtmlFile,
					htmlMan.readFileAsString(htmlHeaderFile)
							+ galleryManager.buildGalleryHtml(localWebRoot,
									galleryDir)
							+ htmlMan.readFileAsString(htmlTrailerFile));
		} catch (final IOException e) {
			throw new MarkIVException("Exception occurred:",e);
		}
	}

	public Properties readProps() {
		Properties props = this.pu.readProperties(PROPS_FILE);
		LOG.info("Properties loaded:");
		for (final Object key : props.keySet()) {
			LOG.info(key + "=" + props.get(key));
		}
		return props;
	}

	public void writeProps(Properties props, String propsFile) {
		this.pu.writeProperties(props, propsFile);
	}
}
