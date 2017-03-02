/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.database;

import com.tetrapak.dashboard.models.InstalledBaseBean;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;

import org.neo4j.driver.v1.exceptions.ClientException;

/**
 * This class runs queries against the Graph database related to sales
 * potentials of Maintenance and Spare Parts to the Installed base.
 *
 * @author SEPALMM
 */
public class Potentials {

    Driver driver = GraphDatabase.driver(Utilities.URI(),
            AuthTokens.basic(Utilities.myUserName(), Utilities.myPassword()));

    /**
     * Loads and creates a Relationship representing Potentials between
     * Assortment Nodes and Customers, having IB. Creates the 'Located_In'
     * relationship between the Customer and the Country.
     *
     * @param installedBaseMap containing the countryISOcode-Key, and Values of
     * countryISOcode, finalCustomerKey, assortmentConsumer, potSpareParts,
     * potMaintenanceHrs, potMaintenance
     */
    public void loadPotentialsData(
            Map<Integer, InstalledBaseBean> installedBaseMap) {
        Map<Integer, InstalledBaseBean> potMap = processPotentials(
                installedBaseMap);

        try (Session session = driver.session()) {

            int eqCounter = 0;

            for (Map.Entry<Integer, InstalledBaseBean> entry : potMap.entrySet()) {
                InstalledBaseBean value = entry.getValue();

                String countryCode = value.getCountryISOcode();
                String customerNumber = value.getFinalCustomerKey();
                String assortmentConsumer = value.getAssortmentConsumer();
                double spEurPotential = value.getPotSpareParts();
                double mtHourPotential = value.getPotMaintenanceHrs();
                double mtEurPotential = value.getPotMaintenance();

                String tx2 = " MATCH (fc:Customer {id: {customerNumber}}),"
                        + " (c:CountryDB {countryId: {countryCode}})"
                        + " MERGE (a:Assortment {name: {assortmentConsumer}})"
                        + " MERGE (fc)-[:LOCATED_IN]->(c)"
                        + " MERGE (a)-[:POTENTIAL_AT {spEurPotential: {spEurPotential}, mtHourPotential: {mtHourPotential}, mtEurPotential: {mtEurPotential}}]->(fc)";

                session.run(tx2, Values.parameters(
                        "customerNumber", customerNumber,
                        "assortmentConsumer", assortmentConsumer,
                        "countryCode", countryCode,
                        "spEurPotential", spEurPotential,
                        "mtHourPotential", mtHourPotential,
                        "mtEurPotential", mtEurPotential
                ));

                eqCounter++;
            }
            System.out.format("Processed %d instances of Potentials.\n",
                    eqCounter);
        } catch (ClientException e) {
            System.err.println("Exception in loadPotentialsData:" + e.
                    getMessage());
            System.exit(5);
        }

    }

    /**
     * Convert the TecBase data in the Installed Base Map to pivot with summed
     * up potentials. (If possible, this could also be done in the TecBase
     * report.)
     *
     * @param installedBaseMap
     * @return summed up Potentials Map
     */
    private static Map<Integer, InstalledBaseBean> processPotentials(
            Map<Integer, InstalledBaseBean> installedBaseMap) {

        Map<Integer, InstalledBaseBean> potentialsMap = new HashMap<>();

        // Compute sum of potentials by Assortment group consumer
        Collection<InstalledBaseBean> myCollection = installedBaseMap.values();
        ArrayList<InstalledBaseBean> ib = new ArrayList<>(
                myCollection);

//  Spare part EUR potentials
        Map<String, Map<String, Map<String, Double>>> spPotMap = ib.stream()
                .collect(Collectors.groupingBy(
                        InstalledBaseBean::getCountryISOcode,
                        Collectors.groupingBy(
                                InstalledBaseBean::getFinalCustomerKey,
                                Collectors.groupingBy(
                                        InstalledBaseBean::getAssortmentConsumer,
                                        Collectors.
                                                summingDouble(
                                                        InstalledBaseBean::getPotSpareParts)))));
//  Maintenance HRS potentials
        Map<String, Map<String, Map<String, Double>>> mtPotHrsMap = ib.stream()
                .collect(Collectors.groupingBy(
                        InstalledBaseBean::getCountryISOcode,
                        Collectors.groupingBy(
                                InstalledBaseBean::getFinalCustomerKey,
                                Collectors.groupingBy(
                                        InstalledBaseBean::getAssortmentConsumer,
                                        Collectors.
                                                summingDouble(
                                                        InstalledBaseBean::getPotMaintenanceHrs)))));
//  Maintenance EUR potentials
        Map<String, Map<String, Map<String, Double>>> mtPotMap = ib.stream()
                .collect(Collectors.groupingBy(
                        InstalledBaseBean::getCountryISOcode,
                        Collectors.groupingBy(
                                InstalledBaseBean::getFinalCustomerKey,
                                Collectors.groupingBy(
                                        InstalledBaseBean::getAssortmentConsumer,
                                        Collectors.
                                                summingDouble(
                                                        InstalledBaseBean::getPotMaintenance)))));

//  Flatten the spPotMap
        for (Map.Entry<String, Map<String, Map<String, Double>>> entry : spPotMap.
                entrySet()) {
            String countryCode = entry.getKey();
            Map<String, Map<String, Double>> custNoMap = entry.getValue();
            for (Map.Entry<String, Map<String, Double>> entry1 : custNoMap.
                    entrySet()) {
                String custNo = entry1.getKey();
                Map<String, Double> consumerMap = entry1.getValue();
                for (Map.Entry<String, Double> entry2 : consumerMap.entrySet()) {
                    String consumer = entry2.getKey();
                    Double potential = entry2.getValue();

//  System.out.printf("%s, %s, %s, %s, %s, %s\n", countryCode, custNo, consumer, potential);
//  Add to potentials map
                    Integer key = (countryCode + custNo + consumer).hashCode();
                    potentialsMap.put(key, new InstalledBaseBean(countryCode,
                            custNo, consumer, potential, 0, 0));
                }
            }
        }

        //  Flatten the mtPotHrsMap
        for (Map.Entry<String, Map<String, Map<String, Double>>> entry : mtPotHrsMap.
                entrySet()) {
            String countryCode = entry.getKey();
            Map<String, Map<String, Double>> custNoMap = entry.getValue();
            for (Map.Entry<String, Map<String, Double>> entry1 : custNoMap.
                    entrySet()) {
                String custNo = entry1.getKey();
                Map<String, Double> consumerMap = entry1.getValue();
                for (Map.Entry<String, Double> entry2 : consumerMap.entrySet()) {
                    String consumer = entry2.getKey();
                    Double potential = entry2.getValue();

//  Add to potentials map
                    Integer key = (countryCode + custNo + consumer).hashCode();
//  Update potential
                    InstalledBaseBean v = potentialsMap.get(key);
                    v.setPotMaintenanceHrs(potential);
                }
            }
        }

//  Flatten the mtPotMap
        for (Map.Entry<String, Map<String, Map<String, Double>>> entry : mtPotMap.
                entrySet()) {
            String countryCode = entry.getKey();
            Map<String, Map<String, Double>> custNoMap = entry.getValue();
            for (Map.Entry<String, Map<String, Double>> entry1 : custNoMap.
                    entrySet()) {
                String custNo = entry1.getKey();
                Map<String, Double> consumerMap = entry1.getValue();
                for (Map.Entry<String, Double> entry2 : consumerMap.entrySet()) {
                    String consumer = entry2.getKey();
                    Double potential = entry2.getValue();

//  Add to potentials map
                    Integer key = (countryCode + custNo + consumer).hashCode();
//  Update potential
                    InstalledBaseBean v = potentialsMap.get(key);
                    v.setPotMaintenance(potential);
                }
            }
        }

        return potentialsMap;
    }

    /**
     * Close the DB driver
     */
    public void closeNeo4jDriver() {
        driver.close();
        Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
        System.out.println(
                timestampEnd + " :: Closed the driver in Potentials class.");
    }

}
