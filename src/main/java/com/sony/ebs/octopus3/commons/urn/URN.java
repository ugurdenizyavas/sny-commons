package com.sony.ebs.octopus3.commons.urn;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a URN, such as urn:book:bookID
 *
 * @author Lemi Orhan Ergin
 */
public interface URN extends Serializable {
    
    String URN_PREFIX = "urn";
    String URN_DELIMITER = ":";

    /**
	 * Type of URN
	 *
	 * @return String
	 */
	public String getType();

	/**
	 * Value of URN
	 *
	 * @return String
	 */
	public List<String> getValues();

    /**
     * Creates path representation of the URN
     * Path always starts with a slash
     *
     * @return Path like "/type/values"
     */
    public String toPath();
}
