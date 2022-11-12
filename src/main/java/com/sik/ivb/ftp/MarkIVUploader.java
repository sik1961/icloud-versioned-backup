package com.sik.ivb.ftp;

/**
 * @author sik
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sik.ivb.exception.MarkIVException;
import com.sik.ivb.utils.PropsUtils;

public class MarkIVUploader {
	private static final Logger LOG = LogManager.getLogger(MarkIVUploader.class);
	private Properties props;
	private PropsUtils pu = new PropsUtils();

	public MarkIVUploader(Properties props) {
		super();
		this.props = props;
	}

	public boolean upload(final String project, final List<String> files) throws MarkIVException {

		boolean uploadSuccessful = true;
		final FTPClient ftp = new FTPClient();
		try {
			// ftp.setPassiveLocalIPAddress(InetAddress.getLocalHost());
			FileInputStream fis = null;

			LOG.debug("Connecting to: " + this.props.getProperty("FtpServer"));
			ftp.connect(this.props.getProperty("FtpServer"));
			LOG.debug("Logging in with id: " + this.props.getProperty("FtpId"));
			boolean loginSucceeded = ftp.login(this.props.getProperty("FtpId"), this.props.getProperty("FtpPw"));
			if (loginSucceeded) {
				LOG.info("Login succeeded");
			} else {
				uploadSuccessful = false;
			}

			for (final String file : files) {
				final String localFile = String.format("%s/%s", this.props.getProperty("LocalWebRoot"), file);
				fis = new FileInputStream(localFile);
				final String remoteFile = String.format("%s/%s", this.props.getProperty("RemoteWebRoot"), file);

				LOG.info(String.format("Uploading %s -> %s", localFile, remoteFile));
				boolean storeSucceeded = ftp.storeFile(remoteFile, fis);
				if (!storeSucceeded) {
					LOG.error("Upload failed - with code: " + ftp.getReplyString());
					uploadSuccessful = false;
					break;
				}
				fis.close();
			}

			if (uploadSuccessful) {
				Properties fileProps = pu.readProperties(props.getProperty("UploadFilesList"));
				boolean fileAdded = false;
				for (String dir : props.getProperty("UploadDirs").split(",")) {

					File folder = new File(props.getProperty("LocalWebRoot") + dir);
					List<String> localFiles = (getFileNames(Arrays.asList(folder.listFiles())));
					String remoteCsv = fileProps.getProperty(dir.replace("/", "."));
					List<String> remoteFiles = null;
					if (remoteCsv != null) {
						remoteFiles = Arrays.asList(remoteCsv.split(","));
					} else {
						remoteFiles = new ArrayList<String>();
					}
					String newFiles = "";
					for (String file : localFiles) {
						if (!remoteFiles.contains(file)) {
							final String localFile = String.format("%s/%s/%s", this.props.getProperty("LocalWebRoot"),
									dir, file);
							fis = new FileInputStream(localFile);
							final String remoteFile = String.format("%s/%s/%s", this.props.getProperty("RemoteWebRoot"),
									dir, file);

							LOG.info(String.format("Uploading %s -> %s", localFile, remoteFile));
							boolean storeSucceeded = ftp.storeFile(remoteFile, fis);
							if (!storeSucceeded) {
								LOG.error("Upload failed - with code: " + ftp.getReplyString());
								uploadSuccessful = false;
								break;
							}
							fis.close();
							newFiles = newFiles + "," + file;
							fileAdded = true;
						}
					}
					if (fileAdded) {
						String propValue = fileProps.getProperty(dir.replace("/", ".")) + newFiles;
						String propKey = dir.replace("/", ".");
						LOG.info("Setting property: " + propKey + " to: " + propValue);
						fileProps.setProperty(propKey, propValue);
					}

				}
				if (fileAdded) {
					LOG.info("Writing properties: " + props.getProperty("UploadFilesList"));
					pu.writeProperties(fileProps, props.getProperty("UploadFilesList"));
				}
			}

			LOG.debug("Logging out...");
			ftp.logout();
			//ftp.disconnect();
			if (uploadSuccessful) {
				LOG.info("Upload completed successfully");
			} else {
				LOG.info("Upload failed");
			}
		} catch (final org.apache.commons.net.ftp.FTPConnectionClosedException cc) {
			throw new MarkIVException("Upload not completed - Connection was closed unexpectedly");
		} catch (final IOException e) {
			throw new MarkIVException("I/O error occurred:" + e.getMessage());
		} catch (final Exception e) {
			throw new MarkIVException("Exception occurred:", e);
		}
		return uploadSuccessful;
	}

	/**
	 * @param files
	 * @return
	 */
	private List<String> getFileNames(List<File> files) {
		List<String> names = new ArrayList<String>();
		for (File f : files) {
			names.add(f.getName());
		}
		return names;
	}
}
