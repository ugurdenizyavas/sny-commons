package com.sony.ebs.octopus3.commons.urn;

import org.junit.Test;

import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Lemi Orhan Ergin
 */
public class URNTest {

    @Test
    public void createFromURNString() throws URNCreationException {
        URN urn = new URNImpl("urn:vm:123456789");
        assertEquals("Type is wrong", "vm", urn.getType());
        assertEquals("Value is wrong", Arrays.asList("123456789"), urn.getValues());
        assertEquals("Generated URN string is wrong", "urn:vm:123456789", urn.toString());
        assertEquals("Generated path string is wrong", "/vm/123456789", urn.toPath());
    }

    @Test
    public void createFromLongerURNString() throws URNCreationException {
        URN urn = new URNImpl("urn:vm:123456789:en_GB");
        assertEquals("Type is wrong", "vm", urn.getType());
        assertEquals("Value is wrong", Arrays.asList("123456789","en_gb"), urn.getValues());
        assertEquals("Generated URN string is wrong", "urn:vm:123456789:en_gb", urn.toString());
        assertEquals("Generated path string is wrong", "/vm/123456789/en_gb", urn.toPath());
    }

    @Test
    public void createURNByValues() throws URNCreationException {
        URN urn = new URNImpl("vm", Arrays.asList("123456789", "en_GB"));
        assertEquals("Type is wrong", "vm", urn.getType());
        assertEquals("Value is wrong", Arrays.asList("123456789","en_gb"), urn.getValues());
        assertEquals("Generated URN string is wrong", "urn:vm:123456789:en_gb", urn.toString());
        assertEquals("Generated path string is wrong", "/vm/123456789/en_gb", urn.toPath());
    }

    @Test
    public void isCaseURNIdenticalWithLowerCaseURN() throws URNCreationException {
        URN urn = new URNImpl("URN:VM:UPPERCASE");
        assertEquals("Type is wrong", "vm", urn.getType());
        assertEquals("Value is wrong", Arrays.asList("uppercase"), urn.getValues());

        URN urn2 = new URNImpl("urn:vm:uppercase");
        assertTrue("URN comparison seem case sensitive urn1 [" + urn + "] and urn2 [" + urn2 + "]", urn2.equals(urn));
    }

    @Test
    public void createFileURN() throws URNCreationException {
        URN urn = new URNImpl(Paths.get("/a/b"), Paths.get("/a/b/c/d/e"));
        assertEquals("Type is wrong", "c", urn.getType());
        assertEquals("Value is wrong", Arrays.asList("d", "e"), urn.getValues());
        assertEquals("Generated URN string is wrong", "urn:c:d:e", urn.toString());
        assertEquals("Generated URN string is wrong", new URNImpl("urn:c:d:e"), urn);
        assertEquals("Generated path string is wrong", "/c/d/e", urn.toPath());
    }

    @Test
    public void validateURNs() throws URNCreationException {
        URNImpl urn = new URNImpl("urn:sku:a");

        // positives
        assertTrue(urn.validateURN("urn:sku:a"));
        assertTrue(urn.validateURN("urn:global_sku:a"));
        assertTrue(urn.validateURN("urn:global-sku:a"));
        assertTrue(urn.validateURN("urn:sku:a.a"));
        assertTrue(urn.validateURN("urn:sku:a-a"));
        assertTrue(urn.validateURN("urn:sku:a%a"));
        assertTrue(urn.validateURN("urn:sku:a+a"));
        assertTrue(urn.validateURN("URN:SKU:A"));
        assertTrue(urn.validateURN("urn:test+sku:a"));
        assertTrue(urn.validateURN("urn:test.sku:a"));

        // negatives
        assertFalse(urn.validateURN("urn"));
        assertFalse(urn.validateURN("urn:sku"));
        assertFalse(urn.validateURN("urn:sku:"));
        assertFalse(urn.validateURN(":sku:a"));
        assertFalse(urn.validateURN("xyz:sku:a"));
        assertFalse(urn.validateURN("urn:sku:a a"));
        assertFalse(urn.validateURN("urn:sku:a/a"));
        assertFalse(urn.validateURN("urn:sku:a\\a"));
        assertFalse(urn.validateURN("urn:sku:a:"));
        assertFalse(urn.validateURN("urn:sku:a:::"));
        assertFalse(urn.validateURN("urn:sku:a:<script>"));
    }

    // ============================
    // ERROR CONDITIONS
    // ============================

    @Test(expected = URNCreationException.class)
    public void cannotCreateFromSingleURNString() throws URNCreationException {
        new URNImpl("urn:vm");
    }

    @Test(expected = URNCreationException.class)
    public void cannotCreateFromSingleURNStringForInvalidPrefix() throws URNCreationException {
        new URNImpl("urx:vm:123456789");
    }

    @Test(expected = URNCreationException.class)
    public void cannotCreateFromNullURNString() throws URNCreationException {
        new URNImpl(null);
    }

    @Test(expected = URNCreationException.class)
    public void cannotCreateFromEmptyURNString() throws URNCreationException {
        new URNImpl("");
    }

    @Test(expected = URNCreationException.class)
    public void cannotCreateFromValues() throws URNCreationException {
        new URNImpl("vm", null);
    }

    @Test(expected = URNCreationException.class)
    public void missingPrefixInURN() throws URNCreationException {
        new URNImpl(":VM:UPPERCASE");
    }

    @Test(expected = URNCreationException.class)
    public void missingTypeInURN() throws URNCreationException {
        new URNImpl("URN::UPPERCASE");
    }

    @Test(expected = URNCreationException.class)
    public void missingValueInURN() throws URNCreationException {
        new URNImpl("URN:VM:");
    }

    @Test(expected = URNCreationException.class)
    public void missingTypeAsParam() throws URNCreationException {
        new URNImpl(null, Arrays.asList("a","b","c"));
    }

    @Test(expected = URNCreationException.class)
    public void missingValuesAsParam() throws URNCreationException {
        new URNImpl("sku", null);
    }

    @Test(expected = URNCreationException.class)
    public void missingValueInValuesParam() throws URNCreationException {
        new URNImpl("sku", Arrays.asList("a", "", "c"));
    }

    @Test(expected = URNCreationException.class)
    public void emptyValuesParam() throws URNCreationException {
        new URNImpl("sku", new ArrayList<String>());
    }

}
