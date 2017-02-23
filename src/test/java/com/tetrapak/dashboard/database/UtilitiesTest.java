/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.database;

import com.tetrapak.dashboard.models.TransactionBean;
import java.time.LocalDate;
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
        assertEquals("Other", Utilities.assignAssortmentGroup(""));
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

    /**
     * Test of URI method, of class Utilities.
     */
    @Ignore
    @Test
    public void testURI() {
        System.out.println("URI");
        String expResult = "";
        String result = Utilities.URI();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of myUserName method, of class Utilities.
     */
    @Ignore
    @Test
    public void testMyUserName() {
        System.out.println("myUserName");
        String expResult = "";
        String result = Utilities.myUserName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of myPassword method, of class Utilities.
     */
    @Ignore
    @Test
    public void testMyPassword() {
        System.out.println("myPassword");
        String expResult = "";
        String result = Utilities.myPassword();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createTPformatCustNo method, of class Utilities.
     */
    @Test
    public void testCreateTPformatCustNo() {
        System.out.println("createTPformatCustNo");
//        assertEquals("Couldn't process input,", expResult, result);
        assertEquals("Couldn't process input,", "0000000123", Utilities.createTPformatCustNo("123"));
        assertEquals("Couldn't process input,", "0000000000", Utilities.createTPformatCustNo("0000000000"));
        assertEquals("Couldn't process input,", "1234567890", Utilities.createTPformatCustNo("1234567890"));
        assertEquals("Couldn't process input,", "#", Utilities.createTPformatCustNo("ab"));
        assertEquals("Couldn't process input,", "#", Utilities.createTPformatCustNo("a123"));
        assertEquals("Couldn't process input,", "#", Utilities.createTPformatCustNo("12345678901"));
        assertEquals("Couldn't process input,", "#", Utilities.createTPformatCustNo(""));
        assertEquals("Couldn't process input,", "#", Utilities.createTPformatCustNo("010-1"));
        
        assertNotEquals("Couldn't process input,", "00000000ab", Utilities.createTPformatCustNo("ab"));
    }

    /**
     * Test of makeCustType method, of class Utilities.
     */
    @Test
    public void testMakeCustType() {
        System.out.println("makeCustType");
//        String result = Utilities.makeCustType(custGroup);
        assertEquals("Global Account", Utilities.makeCustType("DANONE"));
        assertEquals("Int. Account", Utilities.makeCustType("YAKULT"));
        assertEquals("Other", Utilities.makeCustType("DUMMY"));
        assertEquals("Other", Utilities.makeCustType(""));
    }

    /**
     * Test of extractQuantity method, of class Utilities.
     */
    @Test
    public void testExtractQuantity() {
        System.out.println("extractQuantity");
        TransactionBean tr = new TransactionBean();
        
        tr.setInvoiceQuantityT("100.0 PC");
        double expResult = 100.0;
        double result = Utilities.extractQuantity(tr);
        assertEquals(expResult, result, 0.0);
        
        tr.setInvoiceQuantityT("100.0 FT2");
        double expResult1 = 100.0;
        double result1 = Utilities.extractQuantity(tr);
        assertEquals(expResult1, result1, 0.0);
        
        tr.setInvoiceQuantityT("1 00.0 *");
        double expResult2 = 100.0;
        double result2 = Utilities.extractQuantity(tr);
        assertEquals(expResult2, result2, 0.0);
    }

    /**
     * Test of extractQuantityType method, of class Utilities.
     */
    @Test
    public void testExtractQuantityType() {
        System.out.println("extractQuantityType");
        TransactionBean tr = new TransactionBean();
        
        tr.setInvoiceQuantityT("100.0 PC");
        String expResult = "PC";
        String result = Utilities.extractQuantityType(tr);
        assertEquals(expResult, result);
        
        tr.setInvoiceQuantityT("100.0 FT2");
        String expResult1 = "FT2";
        String result1 = Utilities.extractQuantityType(tr);
        assertEquals(expResult1, result1);
        
        tr.setInvoiceQuantityT("1 00.0 *");
        String expResult2 = "*";
        String result2 = Utilities.extractQuantityType(tr);
        assertEquals(expResult2, result2);
        
        tr.setInvoiceQuantityT("1 00.0 ");
        String expResult3 = "";
        String result3 = Utilities.extractQuantityType(tr);
        assertEquals(expResult3, result3);
        
        tr.setInvoiceQuantityT("1 00.0");
        String expResult4 = "";
        String result4 = Utilities.extractQuantityType(tr);
        assertEquals(expResult4, result4);
        
        tr.setInvoiceQuantityT("100.0");
        String expResult5 = "";
        String result5 = Utilities.extractQuantityType(tr);
        assertEquals(expResult5, result5);

    }

}
