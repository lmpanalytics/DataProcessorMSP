/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.database;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author SEPALMM
 */
public class UtilitiesTest {

    public UtilitiesTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of toCypherVariableFormat method, of class Utilities.
     */
    @Test
    public void testToCypherVariableFormat() {
        System.out.println("toCypherVariableFormat");
        String variable = "test";
        String expResult = "'test'";
        String result = Utilities.toCypherVariableFormat(variable);
        assertEquals(expResult, result);
    }

    /**
     * Test of assignAssortmentGroup method, of class Utilities.
     */
    @Test
    public void testAssignAssortmentGroup() {
        System.out.println("assignAssortmentGroup");
        assertEquals("Other", Utilities.assignAssortmentGroup("dummy"));
        assertEquals("Homogeniser parts", Utilities.assignAssortmentGroup("ALEX"));
        assertEquals("Homogeniser parts", Utilities.assignAssortmentGroup("HOMO"));
        assertEquals("Ice cream equipment parts", Utilities.assignAssortmentGroup("ICEXTRUS"));
        assertEquals("Ice cream equipment parts", Utilities.assignAssortmentGroup("ICFILLIN"));
        assertEquals("Ice cream equipment parts", Utilities.assignAssortmentGroup("ICFREEZE"));
        assertEquals("Ice cream equipment parts", Utilities.assignAssortmentGroup("ICFREINC"));
        assertEquals("Ice cream equipment parts", Utilities.assignAssortmentGroup("ICHANDLI"));
        assertEquals("Ice cream equipment parts", Utilities.assignAssortmentGroup("ICINCLUS"));
        assertEquals("Ice cream equipment parts", Utilities.assignAssortmentGroup("ICMIXING"));
        assertEquals("Ice cream equipment parts", Utilities.assignAssortmentGroup("ICMOULD"));
        assertEquals("Ice cream equipment parts", Utilities.assignAssortmentGroup("ICWRAPPI"));
        assertEquals("Plate heat exchanger parts", Utilities.assignAssortmentGroup("PHE"));
        assertEquals("Plate heat exchanger parts", Utilities.assignAssortmentGroup("PHEOLD"));
        assertEquals("Scraped-surface heat exchanger parts", Utilities.assignAssortmentGroup("CONTHERM"));
        assertEquals("Separator parts", Utilities.assignAssortmentGroup("CENTRI"));
        assertEquals("Separator parts", Utilities.assignAssortmentGroup("SEPARATOR"));
        assertEquals("Tubular heat exchanger parts", Utilities.assignAssortmentGroup("SPIRAFLO"));
        assertEquals("Tubular heat exchanger parts", Utilities.assignAssortmentGroup("THE"));
    }

}
