package com.sony.ebs.octopus3.commons.process;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author trerginl
 * @since 02.07.2014
 */
public class ProcessIdTest {

    @Test
    public void create() {
        ProcessId processId = new ProcessIdImpl();
        assertNotNull(processId.getId());
    }

    @Test
    public void createFromString() {
        ProcessId processId = new ProcessIdImpl("abc");
        assertEquals("abc", processId.getId());
    }

    @Test
    public void testToString() {
        ProcessId processId = new ProcessIdImpl("abc");
        assertEquals("ProcessIdImpl(id=abc)", processId.toString());
    }

    @Test
    public void testToStringForNullId() {
        ProcessId processId = new ProcessIdImpl(null);
        assertEquals("ProcessIdImpl(id=)", processId.toString());
    }
}
