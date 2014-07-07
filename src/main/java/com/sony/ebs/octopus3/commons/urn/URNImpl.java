package com.sony.ebs.octopus3.commons.urn;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Default implementation of URN. All data in urn string is converted into lowercase.
 *
 * @author Lemi Orhan Ergin
 */
public class URNImpl implements URN {

    public static final String REGEXP_SECTION = "(?i)([a-z0-9-_+\\.%]+)";
    public static final String REGEXP_URN = "(?i)" + URN_PREFIX + "(" + URN_DELIMITER + REGEXP_SECTION + "){2,}";

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
        if (urnStr == null) {
            throw new URNCreationException("Cannot validate the URN string because it is null");
        }
        process(urnStr);
    }

    /**
     * @param type   type of the urn
     * @param values Values as list of String
     * @throws URNCreationException
     */
    public URNImpl(String type, List<String> values) throws URNCreationException {
        if (type == null || values == null || values.isEmpty()) {
            throw new URNCreationException("Cannot validate the URN because type [" + type + "] or values [" + values + "] is null");
        }
        process(URN_PREFIX + URN_DELIMITER + type + URN_DELIMITER + StringUtils.join(values, URN_DELIMITER));
    }

    /**
     * @param type   type of the urn
     * @param values Values as list of String
     * @throws URNCreationException
     */
    public URNImpl(String type, String... values) throws URNCreationException {
        if (type == null || values == null || values.length == 0) {
            throw new URNCreationException("Cannot validate the URN because type [" + type + "] or values [" + ArrayUtils.toString(values) + "] is null");
        }
        process(URN_PREFIX + URN_DELIMITER + type + URN_DELIMITER + StringUtils.join(values, URN_DELIMITER));
    }

    /**
     * @param parent parent URN, like "urn:a:b" to add c for "urn:a:b:c"
     * @param values Values as list of String
     * @throws URNCreationException
     */
    public URNImpl(URN parent, String... values) throws URNCreationException {
        if (parent == null || values == null || values.length == 0) {
            throw new URNCreationException("Cannot validate the URN because type [" + type + "] or values [" + ArrayUtils.toString(values) + "] is null");
        }
        process(parent.toString() + URN_DELIMITER + StringUtils.join(values, URN_DELIMITER));
    }

    /**
     * This constructor is used for creating URNs for given paths in filesystem. We assume that the base path is
     * a parent of the given file's path.
     *
     * @param base is the path of base folder, like "/home"
     * @param path is the path of the file, like "/home/path/to/file"
     * @throws URNCreationException
     */
    public URNImpl(Path base, Path path) throws URNCreationException {
        if (base == null || path == null) {
            throw new URNCreationException("Cannot validate the URN because base path [" + base + "] or file path [" + path + "] is null");
        }
        process(URN_PREFIX + URN_DELIMITER +
                path.subpath(base.getNameCount(), path.getNameCount()).toString()
                        .replace(File.separator, URN_DELIMITER));
    }

    /**
     * Processes URN string. It is used by constructors.
     *
     * @param urnStr String representation of URN
     * @throws URNCreationException occurs in case of issues
     */
    protected void process(String urnStr) throws URNCreationException {
        List<String> tokens;

        if (!validateURN(urnStr)) {
            throw new URNCreationException("URN string [" + urnStr + "] is invalid");
        }
        tokens = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(urnStr, URN_DELIMITER));
        this.type = tokens.get(1).toLowerCase().intern();
        this.values = lowercase(tokens.subList(2, tokens.size()));
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        URNImpl rhs = (URNImpl) obj;
        return new EqualsBuilder()
                .append(getType(), rhs.getType())
                .append(getValues(), rhs.getValues())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getType())
                .append(getValues())
                .toHashCode();
    }

    /**
     * Validates the incoming URN string
     *
     * @param data string representation of the URN/type/value
     * @return true if it is a valid URN, type or value
     */
    protected boolean validateURN(String data) throws URNCreationException {
        return Pattern.matches(REGEXP_URN, data);
    }


    /**
     * Sanitizes and converts the items in a list to lowercase
     *
     * @param input as values of a URN
     * @return sanitized, lowercase values as list
     * @throws URNCreationException due to missing or wrong data
     */
    protected List<String> lowercase(List<String> input) throws URNCreationException {
        List<String> output = new ArrayList<String>();
        for (String value : input) {
            output.add(value.toLowerCase());
        }
        return output;
    }
}
