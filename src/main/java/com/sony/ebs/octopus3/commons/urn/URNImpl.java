package com.sony.ebs.octopus3.commons.urn;

import org.apache.commons.lang.StringUtils;

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

    public static final String REGEXP_SECTION = "([a-zA-Z0-9-_+\\.%]+)";
    public static final String REGEXP_URN = URN_PREFIX + URN_DELIMITER + REGEXP_SECTION + "(" + URN_DELIMITER + REGEXP_SECTION + ")+";

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
        process(urnStr);
    }

    /**
     * @param type   type of the urn
     * @param values Values as list of String
     * @throws URNCreationException
     */
    public URNImpl(String type, List<String> values) throws URNCreationException {
        if (type == null || values == null || values.isEmpty()){
            throw new URNCreationException("Cannot validate the URN because type ["+type+"] or values ["+values+"] is null");
        }
        process(URN_PREFIX + URN_DELIMITER + type + URN_DELIMITER + StringUtils.join(values, URN_DELIMITER));
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
        if (base == null || path == null){
            throw new URNCreationException("Cannot validate the URN because base path ["+base+"] or file path ["+path+"] is null");
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
        try {
            tokens = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(urnStr, URN_DELIMITER));
            this.type = lowercase(tokens.get(1).intern());
            this.values = lowercase(tokens.subList(2, tokens.size()));
        } catch (Exception e) {
            throw new URNCreationException("Error occurred while creating URN [" + urnStr + "]", e);
        }
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
     * Validates the sections of incoming URN string
     *
     * @param data string representation of the type or value
     * @return true if it is a valid type or value
     */
    protected boolean validateSection(String data) {
        return Pattern.compile(REGEXP_SECTION, Pattern.CASE_INSENSITIVE).matcher(data).matches();
    }

    /**
     * Validates the incoming URN string
     *
     * @param data string representation of the URN/type/value
     * @return true if it is a valid URN, type or value
     */
    protected boolean validateURN(String data) throws URNCreationException {
        if (data == null) throw new URNCreationException("Cannot validate the URN because it is null");
        return Pattern.compile(REGEXP_URN, Pattern.CASE_INSENSITIVE).matcher(data).matches();
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
            output.add(lowercase(value));
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
    protected String lowercase(String input) throws URNCreationException {
        if (!validateSection(input)) {
            throw new URNCreationException("URN section string [" + input + "] is invalid");
        }
        return input.toLowerCase();
    }
}
