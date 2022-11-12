package com.sik.ivb.utils;
/**
 * @author sik
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class PropsUtils {

	public Properties readProperties(String file) {
		Properties props = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			props.load(fis);
		} catch (IOException e) {
			return null;
		}
		return props;
	}
	
	public void writeProperties(Properties props, String file) {
	    try {

	        OutputStream out = new FileOutputStream( file );
	        props.store(out, "Properties file for com.sik.markiv.MarkIV.java");
	    }
	    catch (IOException e ) {
	        e.printStackTrace();
	    }
	}
}
