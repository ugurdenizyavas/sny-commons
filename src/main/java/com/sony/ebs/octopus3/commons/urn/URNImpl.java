package com.sony.ebs.octopus3.commons.urn;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Default implementation of URN. All data in urn string is converted into lowercase.
 *
 * @author Lemi Orhan Ergin
 */
public class URNImpl implements URN {

    private static final Logger logger = LoggerFactory.getLogger(URNImpl.class);
    private String type;
    private List<String> values;

    /**
     * First, the string is converted into URN object. We validate the urn string in this way because, if no exception
     * is thrown, urn is meant to be valid. Then we check the validity of data inside the urn.
     *
     * @param urnStr as string representation of a urn
     * @throws URNCreationException thrown in case of conversion errors
     */
    public URNImpl(String urnStr) throws URNCreationException {
        List<String> tokens;
        String prefix;

        try {
            tokens = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(urnStr, URN_DELIMITER));
            prefix = tokens.get(0).toLowerCase();
            this.type = sanitize(tokens.get(1).intern());
            this.values = sanitize(tokens.subList(2, tokens.size()));
        } catch (Exception e) {
            throw new URNCreationException("Error occurred while creating URN [" + urnStr + "]", e);
        }

        if (!URN_PREFIX.equals(prefix)) {
            throw new URNCreationException("Prefix [" + prefix + "] is invalid for the urn string [" + urnStr + "]");
        }
        if (StringUtils.isEmpty(type)) {
            throw new URNCreationException("Type [" + type + "] is invalid for the urn string [" + urnStr + "]");
        }
    }

    /**
     * @param type   type of the urn
     * @param values Values as list of String
     * @throws URNCreationException
     */
    public URNImpl(String type, List<String> values) throws URNCreationException {
        if (null == type || null == values || values.isEmpty()) {
            throw new URNCreationException("Cannot create URN due to null type or value");
        }
        this.type = sanitize(type).intern();
        this.values = sanitize(values);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public List<String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return URN_PREFIX + URN_DELIMITER + type + URN_DELIMITER + StringUtils.join(values, URN_DELIMITER);
    }

    @Override
    public String toPath() {
        return Paths.get("/" + type + "/" + StringUtils.join(values, "/")).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        URNImpl urn = (URNImpl) o;

        if (type != null ? !type.equals(urn.type) : urn.type != null) return false;
        if (values != null ? !values.equals(urn.values) : urn.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }

    /**
     * Sanitizes and converts the items in a list to lowercase
     *
     * @param input as values of a URN
     * @return sanitized, lowercase values as list
     * @throws URNCreationException due to missing or wrong data
     */
    private List<String> sanitize(List<String> input) throws URNCreationException {
        if (input == null || input.isEmpty()) {
            throw new URNCreationException("Values [" + input + "] is null or empty ");
        }
        List<String> output = new ArrayList<String>();
        for (String value : input) {
            if (StringUtils.isEmpty(value)) {
                throw new URNCreationException("Values [" + values + "] contain an empty token");
            }
            output.add(sanitize(value));
        }
        return output;
    }

    /**
     * Sanitizes the given data
     *
     * @param input as String
     * @return sanitized and lowercase data
     * @throws URNCreationException occurs if the given data is empty
     */
    private String sanitize(String input) throws URNCreationException {
        if (StringUtils.isEmpty(input)) {
            throw new URNCreationException("Input string [" + input + "] cannot be sanitized because it is empty");
        }
        try {
            return URLEncoder.encode(input, "UTF-8").toLowerCase();
        } catch (UnsupportedEncodingException e) {
            logger.warn("Input string [" + input + "] cannot be sanitized", e);
            return input;
        }
    }
}
