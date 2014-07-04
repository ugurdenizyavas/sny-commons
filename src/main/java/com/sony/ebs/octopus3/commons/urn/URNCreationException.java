package com.sony.ebs.octopus3.commons.urn;

/**
 * The exception is thrown in case of any errors during creation process.
 *
 * @author Lemi Orhan Ergin
 */
public class URNCreationException extends Exception {

	private static final long serialVersionUID = -4806699576411812556L;

    /**
	 * Constructor that creates a new URNCreationException instance.
	 *
	 * @param message of type String
	 */
	public URNCreationException(String message) {
        super(message);
    }
}
