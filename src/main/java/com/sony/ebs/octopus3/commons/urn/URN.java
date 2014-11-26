package com.sony.ebs.octopus3.commons.urn;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a URN, such as urn:book:bookID. For more details about the specification, please check the links below.
 * 1) http://en.wikipedia.org/wiki/Uniform_resource_name
 * 2) http://www.ietf.org/rfc/rfc2141.txt
 * <p/>
 * A URN is consists of 3 main sections:
 * 1) First "urn" prefix common among all URNs
 * 2) Type information as the next section after "urn" prefix. It defines the type of the item URN represents.
 * The values could be "sku", "global_sku", "amazon_category", etc.
 * 3) Values as list of information after the type data. The order is important, so urn:sku:a:b:c might most probably
 * be interpreted as different than urn:sku:b:c:a.
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
     * Values of URN describing the item. The order is important.
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

    /**
     * Gets and validates parent urn
     *
     * @return URN of the parent
     */
    public URN getParent() throws URNCreationException;


}
