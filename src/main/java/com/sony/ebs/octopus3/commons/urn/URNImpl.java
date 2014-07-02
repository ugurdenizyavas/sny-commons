package com.sony.ebs.octopus3.commons.urn;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Lemi Orhan Ergin
 */
public class URNImpl implements URN {

    public static final Logger logger = LoggerFactory.getLogger(URNImpl.class);

    private String type;
    private List<String> values;

    public URNImpl(String urnStr) throws URNCreationException {
        List<String> tokens;
        try {
            tokens = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(urnStr, URN_DELIMITER));
            this.type = URLEncoder.encode(tokens.get(1).intern(), "UTF-8").toLowerCase();

            List<String> loweredList = new ArrayList<String>();
            for (String value : tokens.subList(2,tokens.size())) {
                loweredList.add(URLEncoder.encode(value, "UTF-8").toLowerCase());
            }
            this.values = loweredList;
        } catch (Exception e) {
            throw new URNCreationException("Error occurred while creating URN [" + urnStr + "]", e);
        }

        if (!URN_PREFIX.equals(tokens.get(0).toLowerCase())) {
            throw new URNCreationException("Prefix [" + tokens.get(0).toLowerCase() + "] is invalid for the urn string [" + urnStr + "]");
        }
        if (StringUtils.isEmpty(type) || values == null || values.isEmpty()) {
            throw new URNCreationException("Values [" + values + "] or Type [" + type + "] is invalid for the urn string [" + urnStr + "]");
        }
        for (String value : values) {
            if (StringUtils.isEmpty(value)) {
                throw new URNCreationException("Values [" + values + "] contain an empty token");
            }
        }
    }

    /**
     * @param type type of the urn
     * @param values Values as list of String
     * @throws URNCreationException
     */
    public URNImpl(String type, List<String> values) throws URNCreationException {
        if (null == type || null == values || values.isEmpty()) {
            logger.error("Cannot create URN for type {} and value {}", type, values);
            throw new URNCreationException("Cannot create URN due to null type or value");
        }
        this.type = type.toLowerCase().intern();
        this.values = values;
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
        return File.separator + type + File.separator + StringUtils.join(values, File.separator);
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
}
