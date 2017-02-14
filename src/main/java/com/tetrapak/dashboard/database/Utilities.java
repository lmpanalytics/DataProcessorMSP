/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.database;

/**
 * This is a utility class
 *
 * @author SEPALMM
 */
public class Utilities {

    /**
     * Method to return the URI of the database driver
     *
     * @return URI
     */
    public static String URI() {
        return "bolt://localhost:7687";
    }
    /**
     * Method to return the user name to the database driver
     *
     * @return User name
     */
    public static String myUserName() {
        return "neo4j";
    }
    /**
     * Method to return the password to the database driver
     *
     * @return password
     */
    public static String myPassword() {
        return "Tokyo2000";
    }

    /**
     * Helper method that changes the format of a text variable from x to 'x'.
     * This is how text properties are formated in Cypher.
     *
     * @param variable the string variable to reformat
     * @return the reformatted variable
     */
    public static String toCypherVariableFormat(String variable) {
        return "'" + variable + "'";
    }

    /**
     * Assign the Spare Part Assortment that is consumed by this Equipment based
     * on the Catalogue Profile
     *
     * @param cat CatalogueProfile of Equipment in TecBase
     * @return Assortment
     */
    public static String assignAssortmentGroup(String cat) {
        String assortment = "Other";
        if (cat.equals("ALEX") || cat.equals("HOMO")) {
            assortment = "Homogeniser parts";
        } else if (cat.equals("ICEXTRUS") || cat.equals("ICFILLIN")
                || cat.equals("ICFREEZE") || cat.equals("ICFREINC")
                || cat.equals("ICHANDLI") || cat.equals("ICINCLUS")
                || cat.equals("ICMIXING") || cat.equals("ICMOULD")
                || cat.equals("ICWRAPPI")) {
            assortment = "Ice cream equipment parts";
        } else if (cat.equals("PHE") || cat.equals("PHEOLD")) {
            assortment = "Plate heat exchanger parts";
        } else if (cat.equals("CONTHERM")) {
            assortment = "Scraped-surface heat exchanger parts";
        } else if (cat.equals("CENTRI") || cat.equals("SEPARATOR")) {
            assortment = "Separator parts";
        } else if (cat.equals("SPIRAFLO") || cat.equals("THE")) {
            assortment = "Tubular heat exchanger parts";
        } else {
            assortment = "Other";
        }
        return assortment;
    }
}
