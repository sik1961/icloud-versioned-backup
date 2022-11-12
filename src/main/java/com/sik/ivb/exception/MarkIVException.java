package com.sik.ivb.exception;
/**
 * @author sik
 *
 */

public class MarkIVException extends RuntimeException {

	private static final long serialVersionUID = -8820713842613218791L;

	public MarkIVException(String message){
        super(message);
    }
	
	public MarkIVException(String message, Throwable cause) {
        super(message, cause);
    }
}
