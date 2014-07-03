package com.sony.ebs.octopus3.commons.date;

/**
 * The exception thrown only in case of failures during date conversion.
 *
 * @author trerginl
 * @since 03.07.2014
 */
public class DateConversionException extends Exception {

	private static final long serialVersionUID = -4806699576411812556L;

	/**
	 * Constructor that creates a new DateConversionException instance.
	 *
	 * @param message of type String
	 * @param cause of type Throwable
	 */
	public DateConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructor that creates a new DateConversionException instance.
	 *
	 * @param message of type String
	 */
	public DateConversionException(String message) {
        super(message);
    }
}
