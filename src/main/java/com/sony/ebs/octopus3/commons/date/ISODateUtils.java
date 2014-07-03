package com.sony.ebs.octopus3.commons.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Only Joda-Time is used to define dates and do date arithmetic.
 * The expected format is "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
 * For more details about ISO-8601 format, please check http://en.wikipedia.org/wiki/ISO_8601
 *
 * @author trerginl
 * @since 02.07.2014
 */
public class ISODateUtils {

    /**
     * Converts the given date string into ISO-8601 format. Normally a runtime exception is thrown in case of failures
     * during the conversion. We catch and throw a higher level checked exception to force users handle these cases.
     *
     * @param dateStr expected format is "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
     * @return Joda-Time's DateTime
     * @throws DateConversionException is thrown in case of a failure in the conversion process
     */
    public static DateTime toISODate(String dateStr) throws DateConversionException {
        try {
            DateTimeFormatter parser = ISODateTimeFormat.dateTime();
            return parser.parseDateTime(dateStr);
        } catch (IllegalArgumentException e) {
            throw new DateConversionException("Date string [" + dateStr + "] cannot be converted to ISO-8601 date format [yyyy-MM-dd'T'HH:mm:ss.SSSZ]", e);
        }
    }

    /**
     * Converts Joda-Time's DateTime into string representation.
     *
     * @param dateTime as Joda-Time's DateTime
     * @return String in "yyyy-MM-dd'T'HH:mm:ss.SSSZ" format
     */
    public static String toISODateString(DateTime dateTime) {
        return dateTime.toString(ISODateTimeFormat.dateTime());
    }

}
