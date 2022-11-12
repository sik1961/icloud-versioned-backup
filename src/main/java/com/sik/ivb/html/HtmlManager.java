package com.sik.ivb.html;
/**
 * @author sik
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HtmlManager {

    public HtmlManager() {}

    public String readFileAsString(final String fn) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final BufferedReader br = new BufferedReader(new FileReader(fn));
        try {
            String line = br.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return sb.toString();
    }

    public void writeHtmlFile(final String fileName, final String html) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(html);
        } catch (final IOException e) {
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (final IOException e) {
            }
        }
    }
}
