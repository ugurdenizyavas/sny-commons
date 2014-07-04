package com.sony.ebs.octopus3.commons.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ISODateUtilsTest {

    @Test
    public void testToISODate() throws DateConversionException {
        assertTrue(new DateTime(2005, 3, 26, 12, 0, 0, 0, DateTimeZone.forOffsetHours(2))
                .isEqual(ISODateUtils.toISODate("2005-03-26T12:00:00.000+02:00")));
    }

    @Test (expected = DateConversionException.class)
    public void emptyDateString() throws DateConversionException {
        ISODateUtils.toISODate("");
    }

    @Test (expected = DateConversionException.class)
    public void nullDateString() throws DateConversionException {
        ISODateUtils.toISODate(null);
    }

    @Test (expected = DateConversionException.class)
    public void testToISODate_forInvalidString() throws Exception {
        ISODateUtils.toISODate("2005-03-26");
    }

    @Test
    public void testToISODateString() throws Exception {
        assertEquals("2005-03-26T12:00:00.000Z",
                ISODateUtils.toISODateString(new DateTime(2005, 3, 26, 12, 0, 0, 0, DateTimeZone.UTC)));
    }

    @Test (expected = DateConversionException.class)
    public void nullDate() throws DateConversionException {
        ISODateUtils.toISODateString(null);
    }

    @Test (expected = InstantiationException.class)
    public void utilityClassCheck() throws Throwable {
        try {
            Constructor c = Class.forName(ISODateUtils.class.getName()).getDeclaredConstructor();
            c.setAccessible(true);
            c.newInstance();
        } catch(InvocationTargetException e) {
            throw e.getTargetException();
            // no need to expect reflection errors
            // we are interested in our own exceptions
        }
    }


}