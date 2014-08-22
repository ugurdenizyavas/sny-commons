package com.sony.ebs.octopus3.commons.urn;

import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals("Generated path string is wrong", Paths.get("/vm/123456789").toString(), urn.toPath());
    }

    @Test
    public void createFromLongerURNString() throws URNCreationException {
        URN urn = new URNImpl("urn:vm:123456789:en_GB");
        assertEquals("Type is wrong", "vm", urn.getType());
        assertEquals("Value is wrong", Arrays.asList("123456789", "en_gb"), urn.getValues());
        assertEquals("Generated URN string is wrong", "urn:vm:123456789:en_gb", urn.toString());
        assertEquals("Generated path string is wrong", Paths.get("/vm/123456789/en_gb").toString(), urn.toPath());
    }

    @Test
    public void createURNByValues() throws URNCreationException {
        URN urn = new URNImpl("vm", Arrays.asList("123456789", "en_GB"));
        assertEquals("Type is wrong", "vm", urn.getType());
        assertEquals("Value is wrong", Arrays.asList("123456789", "en_gb"), urn.getValues());
        assertEquals("Generated URN string is wrong", "urn:vm:123456789:en_gb", urn.toString());
        assertEquals("Generated path string is wrong", Paths.get("/vm/123456789/en_gb").toString(), urn.toPath());
    }

    @Test
    public void createURNByVarArgValues() throws URNCreationException {
        URN urn = new URNImpl("vm", "123456789", "en_GB");
        assertEquals("Type is wrong", "vm", urn.getType());
        assertEquals("Value is wrong", Arrays.asList("123456789", "en_gb"), urn.getValues());
        assertEquals("Generated URN string is wrong", "urn:vm:123456789:en_gb", urn.toString());
        assertEquals("Generated path string is wrong", Paths.get("/vm/123456789/en_gb").toString(), urn.toPath());
    }

    @Test
    public void createURNByValuesWithParent() throws URNCreationException {
        URN parent = new URNImpl("amazon_feed", "global", "en_GB", "KaFa-1500");
        URN urn = new URNImpl(parent, "product_info.csv");
        assertEquals("Type is wrong", "amazon_feed", urn.getType());
        assertEquals("Value is wrong", Arrays.asList("global", "en_gb", "kafa-1500", "product_info.csv"), urn.getValues());
        assertEquals("Generated URN string is wrong", "urn:amazon_feed:global:en_gb:kafa-1500:product_info.csv", urn.toString());
        assertEquals("Generated path string is wrong", Paths.get("/amazon_feed/global/en_gb/kafa-1500/product_info.csv").toString(), urn.toPath());
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
        assertEquals("Generated path string is wrong", Paths.get("/c/d/e").toString(), urn.toPath());

        assertEquals("Equals logic of URN is wrong", new URNImpl("urn:c:d:e"), urn);
        assertEquals("Hash code of URN string is wrong", new URNImpl("urn:c:d:e").hashCode(), urn.hashCode());
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
        assertFalse(urn.validateURN("urn:sku:a:a/b:b"));
        assertFalse(urn.validateURN("urn:sku:a:a..b:b"));
    }

    // ============================
    // ERROR CONDITIONS
    // ============================

    @Test(expected = URNCreationException.class)
    public void urnString_missingValues() throws URNCreationException {
        new URNImpl("urn:vm");
    }

    @Test(expected = URNCreationException.class)
    public void urnString_invalidPrefix() throws URNCreationException {
        new URNImpl("urx:vm:123456789");
    }

    @Test(expected = URNCreationException.class)
    public void urnString_nullString() throws URNCreationException {
        new URNImpl(null);
    }

    @Test(expected = URNCreationException.class)
    public void urnString_emptyString() throws URNCreationException {
        new URNImpl("");
    }

    @Test(expected = URNCreationException.class)
    public void urnString_missingType() throws URNCreationException {
        new URNImpl("URN::UPPERCASE");
    }

    @Test(expected = URNCreationException.class)
    public void urnString_missingValuesInInvalidString() throws URNCreationException {
        new URNImpl("URN:VM:");
    }

    @Test(expected = URNCreationException.class)
    public void urnString_missingPrefix() throws URNCreationException {
        new URNImpl(":VM:UPPERCASE");
    }

    @Test(expected = URNCreationException.class)
    public void urnTypeAndValues_missingValuesArgs() throws URNCreationException {
        new URNImpl("vm", (String[]) null);
    }

    @Test(expected = URNCreationException.class)
    public void urnTypeAndValues_missingValuesList() throws URNCreationException {
        new URNImpl("vm", (List<String>) null);
    }

    @Test(expected = URNCreationException.class)
    public void urnTypeAndValues_missingType() throws URNCreationException {
        new URNImpl(null, Arrays.asList("a", "b", "c"));
    }

    @Test(expected = URNCreationException.class)
    public void urnTypeAndValues_emptyValueInValues() throws URNCreationException {
        new URNImpl("sku", Arrays.asList("a", "", "c"));
    }

    @Test(expected = URNCreationException.class)
    public void urnTypeAndValues_fullyEmptyValues() throws URNCreationException {
        new URNImpl("sku", new ArrayList<String>());
    }

    @Test(expected = URNCreationException.class)
    public void urnFromPaths_missingBasePath() throws URNCreationException {
        new URNImpl(null, Paths.get("/"));
    }

    @Test(expected = URNCreationException.class)
    public void urnFromPaths_missingFilePath() throws URNCreationException {
        new URNImpl(Paths.get("/"), null);
    }

}
