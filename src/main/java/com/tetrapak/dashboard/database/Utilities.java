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
     * Helper method that changes the format of a text variable from x to 'x'.
     * This is how text properties are formated in Cypher.
     *
     * @param variable the string variable to reformat
     * @return the reformatted variable
     */
    public static String toCypherVariableFormat(String variable) {
        return "'" + variable + "'";
    }

}
