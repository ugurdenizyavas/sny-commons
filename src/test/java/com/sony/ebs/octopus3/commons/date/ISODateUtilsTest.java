package com.sony.ebs.octopus3.commons.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.*;

public class ISODateUtilsTest {

    @Test
    public void testToISODate() throws Exception {
        assertEquals(new DateTime(2005, 3, 26, 12, 0, 0, 0, DateTimeZone.forID("Europe/Istanbul")),
                ISODateUtils.toISODate("2005-03-26T12:00:00.000+02:00"));
    }

    @Test
    public void testToISODateString() throws Exception {
        assertEquals("2005-03-26T12:00:00.000Z",
                ISODateUtils.toISODateString(new DateTime(2005, 3, 26, 12, 0, 0, 0, DateTimeZone.UTC)));
    }
}