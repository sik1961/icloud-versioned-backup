package com.sik.ivb.html;

public class HtmlSnippets {
	public static final String HTML_BREAK = "<br>";
	public static final String HTML_HR = "<hr>";
	public static final String OPN_BKT = "(";
	public static final String CLS_BKT = ")";
	public static final String SLASH = "/";
	public static final String N1 = "<n1>";
	public static final String B1 = "<b1>";
	public static final String N1_END = "</n1>";
	public static final String B1_END = "</b1>";
	public static final String NBSP = "&nbsp;";
	public static final String NEWLINE = "\n";
	public static final String NO_GIGS = "<tr><td colspan=\"2\">No Gigs</td></tr>";
	public static final String AVAIL_HEAD = B1 + "Mark IV Availability" + B1_END + NBSP + N1 + "* Recently updated" + N1_END;
	public static final String AVAIL_FMT = "<li>%s - %s %s";
	public static final String GIG1_FORMAT = "<tr><td align=\"right\"><strong>%s - </strong></td><td><strong>%s</strong></td></tr>";
	public static final String GIG2_FORMAT = "<tr><td align=\"right\"></td><td>%s</td></tr>";
	public static final String GIG_HEAD_FMT1 = "<center><b2>Gigs</b2>";
	public static final String GIG_HEAD_FMT2 = "<table align=\"center\" border=\"0\">\n";
	public static final String GIG_HEAD_FMT3 = " <tr>\n";
	public static final String GIG_HEAD_FMT4 = "  <td align=\"right\"><n1>Date/Time - </n1></td>\n";
	public static final String GIG_HEAD_FMT5 = "  <td align=\"left\"><n1>Venue(Details)</n1></td><br>\n";
	public static final String GIG_HEAD_FMT6 = " </tr>\n";
	
	public static final String NEWS_FORMAT = "<li>Our next gig is on <strong>%s</strong> at <strong>%s</strong> - Hope to see you there<br><br>";
	public static final String NEWS_HEAD_FMT1 = "<tr><td><center>" + NEWLINE;
	public static final String NEWS_HEAD_FMT2 = "<img src=\"images/m4front.jpg\" width=\"50%\" height=\"50%\"><br>\n" + NEWLINE;
	public static final String NEWS_HEAD_FMT3 = " <n1>Photo courtesy of " + NEWLINE;
	public static final String NEWS_HEAD_FMT4 = "   <a href=\"http://www.elementalcore.com\" target=\"_blank\">Ian Jukes Photography</a>" + NEWLINE;
	public static final String NEWS_HEAD_FMT5 = " </td>" + NEWLINE;
	public static final String NEWS_HEAD_FMT6 = "<tr><td><center><b2>News</b2></center></td></tr>";
	public static final String NEWS_HEAD_FMT7 = "</tr>" + NEWLINE + "<tr><td><center>";
	public static final String LAST_UPDATE_FORMAT = "<tr><td><center><p>Page last updated at %s</p></center></td></tr>";
	public static final String NEWS_TAIL_FMT8 = "</td></tr></table>" + NEWLINE;
	
	public static final String FONT_COLOR_FMT = "<font color=\"%s\">";
	public static final String FONT_END = "</font>";
	public static final String LAST_UPD_FMT = HTML_BREAK + "<center><p>Page updated %s</p></center>";
	public static final String CAL_LAST_UPD_FMT = "Calendar Last Updated %s"; 
	public static final String TD = "<td>" ;
	public static final String TR = "<tr>" ;
	public static final String TABLE = "<table>";
	public static final String TD_END = "</td>" ;
	public static final String TR_END = "</tr>" ;
	public static final String TABLE_END = "</table>";
	public static final String RED = "#FF8888";
	public static final String AMBER = "#FFFF88";
	public static final String GREEN = "#88FF88";
	public static final String POUND = "&pound;";
	public static final String EMPTY_STRING = "";
	public static final String COMMA = ", ";
	public static final String DOUBLE_BACKSLASH = "\\";
	public static final String DOUBLE_BACKSLASH_N = "\\n";
	public static final String GBP = "GBP";
	
	public static String cleanForHtml(final String s) {
		return s != null ? s.replace(DOUBLE_BACKSLASH_N, COMMA).replace(DOUBLE_BACKSLASH, EMPTY_STRING)
				.replace(GBP, HtmlSnippets.POUND) : null;
	}
}
