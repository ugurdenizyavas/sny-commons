package com.sony.ebs.octopus3.commons.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * The expected format is "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
 * For more details about ISO8601 format, please check http://en.wikipedia.org/wiki/ISO_8601
 *
 * @author trerginl
 * @since 02.07.2014
 */
public class ISODateUtils {

    public static DateTime toISODate(String dateStr) {
        DateTimeFormatter parser = ISODateTimeFormat.dateTime();
        return parser.parseDateTime(dateStr);
    }

    public static String toISODateString(DateTime dateTime) {
        return dateTime.toString(ISODateTimeFormat.dateTime());
    }

}
