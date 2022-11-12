/**
 * 
 */
package com.sik.ivb.utils;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class M4EmailService {

	public static void sendMail(String to, String from, String subject,
			String msgText) {

		String host = "localhost";
		
		boolean sessionDebug = false;
		// Create some properties and get the default Session.
		Properties props = System.getProperties();
		props.put("mail.host", host);
		props.put("mail.transport.protocol", "smtp");
		Session session = Session.getDefaultInstance(props, null);

		session.setDebug(sessionDebug);
		try {
		// Instantiate a new MimeMessage and fill it with the
		// required information.
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		InternetAddress[] address = {new InternetAddress(to)};
		msg.setRecipients(Message.RecipientType.TO, address);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		msg.setText(msgText);
		// Hand the message to the default transport service
		// for delivery.
		Transport.send(msg);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
