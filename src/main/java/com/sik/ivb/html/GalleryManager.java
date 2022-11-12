/**
 * @author sik
 */
package com.sik.ivb.html;

import java.io.File;

public class GalleryManager {

    //private static final char SPACE = 32;
    private static final char DQUOTE = 34;
    //private static final char SQUOTE = 39;
    //private static final char COMMA = 44;
    private static final char SLASH = 47;
    //private static final char SEMICOLON = 59;
    private static final char TAGOPEN = 60;
    private static final char TAGSHUT = 62;
    //private static final char BACKTICK = 96;
    private static final char LF = 10;
    private static final String HTML_TOP1 = "<tr><td>\n";
    private static final String HTML_TOP2 = "<center><b2>Gallery</b2><br>Our thanks go to our official photographers;";
    private static final String HTML_TOP3 = "<a href=\"http://www.elementalcore.com\" target=\"_blank\">" + 
    		"Ian Jukes Photography</a>, Zak K, Joe K & Steve B";
    private static final String HTML_TOP4 = "</td></tr>\n";

    public String buildGalleryHtml(final String localWebRoot, final String galleryDir) {
        final File folder = new File(localWebRoot + galleryDir);
        final File[] listOfFiles = folder.listFiles();

        final StringBuilder sb = new StringBuilder();

        sb.append(HTML_TOP1).append(HTML_TOP2).append(HTML_TOP3).append(HTML_TOP4).append(HTML_TOP1);
        for (final File f : listOfFiles) {
            if (f.isFile() && !f.getName().startsWith(".")) {
                sb.append(TAGOPEN).append("a href=").append(DQUOTE).append(galleryDir).append(SLASH).append(f.getName())
                    .append(DQUOTE).append(" target=\"_blank\"").append(TAGSHUT).append(LF);

                sb.append("\t").append(TAGOPEN).append("img src=").append(DQUOTE).append(galleryDir).append(SLASH)
                    .append(f.getName()).append(DQUOTE).append(" width=\"10%\" height=\"10%\"").append(TAGSHUT).append(LF);

                sb.append(TAGOPEN).append(SLASH).append("a").append(TAGSHUT).append(LF);
            }
        }
        sb.append(HTML_TOP4);

        return sb.toString();
    }
}
