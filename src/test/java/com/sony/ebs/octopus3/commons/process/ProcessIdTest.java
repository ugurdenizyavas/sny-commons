package com.sony.ebs.octopus3.commons.process;

import org.junit.Test;

import static org.junit.Assert.*;

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
        assertTrue(processId.toString().matches("ProcessIdImpl\\(id=.*\\)"));
    }

    @Test
    public void testEqualsEqual() {
        assertEquals(new ProcessIdImpl("123"), new ProcessIdImpl("123"));
    }

    @Test
    public void testEqualsNotEqual() {
        assertNotEquals(new ProcessIdImpl("123"), new ProcessIdImpl("12"));
    }
    @Test
    public void testEqualsNull() {
        assertNotEquals(new ProcessIdImpl("123"), null);
    }
}
