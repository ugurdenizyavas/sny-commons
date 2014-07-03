package com.sony.ebs.octopus3.commons.urn;

import org.junit.Test;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
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
        assertTrue("URN comparison seem case sensitive urn1 ["+urn+"] and urn2 ["+urn2+"]", urn2.equals(urn));
    }

    @Test
    public void specialCharacters_slash() throws URNCreationException {
        URN urn = new URNImpl("urn:global_sku:global:en_GB:DSCT800/T.CEE");
        assertEquals("Generated URN string is wrong", "urn:global_sku:global:en_gb:dsct800%2ft.cee", urn.toString());
        assertEquals("Generated path string is wrong", "/global_sku/global/en_gb/dsct800%2ft.cee", urn.toPath());
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
