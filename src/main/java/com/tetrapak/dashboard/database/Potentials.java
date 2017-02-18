/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.database;

import com.tetrapak.dashboard.models.InstalledBaseBean;
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
     * Loads and creates Nodes for Installed Base representing potentials. An
     * exception is thrown and the program exits in case the value-data change
     * in the Material Map vs. the database content.
     *
     * @param installedBaseMap containing the countryISOcode-Key, and Values of
     * countryISOcode, finalCustomerKey, finalCustomerName, customerGroup,
     * assortmentConsumer, potSpareParts, potMaintenanceHrs, potMaintenance
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

                String tx2 = " MATCH (fc:Customer {id: {customerNumber}})"
                        + " MATCH (c:Country {countryId: {countryCode}})"
                        + " MERGE (fc)-[:LOCATED_IN]->(c)"
                        + " MERGE (ib:InstalledBase {id: {customerNumber}})-[:POTENTIAL {spEurPotential: {spEurPotential}, mtHourPotential: {mtHourPotential}, mtEurPotential: {mtEurPotential}}]->(fc)";

                session.run(tx2, Values.parameters(
                        "customerNumber", customerNumber,
                        "countryCode", countryCode,
                        "spEurPotential", spEurPotential,
                        "mtHourPotential", mtHourPotential,
                        "mtEurPotential", mtEurPotential
                ));

                String tx3 = " MATCH (a:Assortment {name: {assortmentConsumer}})"
                        + " MATCH (ib:InstalledBase {id: {customerNumber}})"
                        + " MERGE (a)-[:FOR]->(ib)";

                session.run(tx3, Values.parameters(
                        "assortmentConsumer", assortmentConsumer,
                        "customerNumber", customerNumber
                ));

                eqCounter++;
            }
            System.out.format("Processed %d instances of Potentials.\n",
                    eqCounter);
        } catch (ClientException e) {
            System.err.println("Exception in loadPotentialsData:" + e.getMessage());
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
        Map<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> spPotMap = ib.
                stream()
                .collect(Collectors.groupingBy(
                        InstalledBaseBean::getCountryISOcode,
                        Collectors.groupingBy(
                                InstalledBaseBean::getFinalCustomerKey,
                                Collectors.groupingBy(
                                        InstalledBaseBean::getFinalCustomerName,
                                        Collectors.groupingBy(
                                                InstalledBaseBean::getCustomerGroup,
                                                Collectors.groupingBy(
                                                        InstalledBaseBean::getAssortmentConsumer,
                                                        Collectors.
                                                                summingDouble(
                                                                        InstalledBaseBean::getPotSpareParts)))))));
//  Maintenance HRS potentials
        Map<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> mtPotHrsMap = ib.
                stream()
                .collect(Collectors.groupingBy(
                        InstalledBaseBean::getCountryISOcode,
                        Collectors.groupingBy(
                                InstalledBaseBean::getFinalCustomerKey,
                                Collectors.groupingBy(
                                        InstalledBaseBean::getFinalCustomerName,
                                        Collectors.groupingBy(
                                                InstalledBaseBean::getCustomerGroup,
                                                Collectors.groupingBy(
                                                        InstalledBaseBean::getAssortmentConsumer,
                                                        Collectors.
                                                                summingDouble(
                                                                        InstalledBaseBean::getPotMaintenanceHrs)))))));
//  Maintenance EUR potentials
        Map<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> mtPotMap = ib.
                stream()
                .collect(Collectors.groupingBy(
                        InstalledBaseBean::getCountryISOcode,
                        Collectors.groupingBy(
                                InstalledBaseBean::getFinalCustomerKey,
                                Collectors.groupingBy(
                                        InstalledBaseBean::getFinalCustomerName,
                                        Collectors.groupingBy(
                                                InstalledBaseBean::getCustomerGroup,
                                                Collectors.groupingBy(
                                                        InstalledBaseBean::getAssortmentConsumer,
                                                        Collectors.
                                                                summingDouble(
                                                                        InstalledBaseBean::getPotMaintenance)))))));

//  Flatten the spPotMap
        for (Map.Entry<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> entry : spPotMap.
                entrySet()) {
            String countryCode = entry.getKey();
            Map<String, Map<String, Map<String, Map<String, Double>>>> custNoMap = entry.
                    getValue();
            for (Map.Entry<String, Map<String, Map<String, Map<String, Double>>>> entry1 : custNoMap.
                    entrySet()) {
                String custNo = entry1.getKey();
                Map<String, Map<String, Map<String, Double>>> custNameMap = entry1.
                        getValue();
                for (Map.Entry<String, Map<String, Map<String, Double>>> entry2 : custNameMap.
                        entrySet()) {
                    String custName = entry2.getKey();
                    Map<String, Map<String, Double>> custGrpMap = entry2.
                            getValue();
                    for (Map.Entry<String, Map<String, Double>> entry3 : custGrpMap.
                            entrySet()) {
                        String custGrp = entry3.getKey();
                        Map<String, Double> consumerMap = entry3.getValue();
                        for (Map.Entry<String, Double> entry4 : consumerMap.
                                entrySet()) {
                            String consumer = entry4.getKey();
                            Double potential = entry4.getValue();

//                            System.out.printf("%s, %s, %s, %s, %s, %s\n", countryCode, custNo, custName, custGrp, consumer, potential);
//                            Add to potentials map
                            Integer key = (countryCode + custNo + custName
                                    + custGrp + consumer).hashCode();
                            potentialsMap.put(key, new InstalledBaseBean(
                                    countryCode, custNo, custName, custGrp,
                                    consumer, potential, 0, 0));

                        }
                    }
                }
            }

        }

        //  Flatten the mtPotHrsMap
        for (Map.Entry<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> entry : mtPotHrsMap.
                entrySet()) {
            String countryCode = entry.getKey();
            Map<String, Map<String, Map<String, Map<String, Double>>>> custNoMap = entry.
                    getValue();
            for (Map.Entry<String, Map<String, Map<String, Map<String, Double>>>> entry1 : custNoMap.
                    entrySet()) {
                String custNo = entry1.getKey();
                Map<String, Map<String, Map<String, Double>>> custNameMap = entry1.
                        getValue();
                for (Map.Entry<String, Map<String, Map<String, Double>>> entry2 : custNameMap.
                        entrySet()) {
                    String custName = entry2.getKey();
                    Map<String, Map<String, Double>> custGrpMap = entry2.
                            getValue();
                    for (Map.Entry<String, Map<String, Double>> entry3 : custGrpMap.
                            entrySet()) {
                        String custGrp = entry3.getKey();
                        Map<String, Double> consumerMap = entry3.getValue();
                        for (Map.Entry<String, Double> entry4 : consumerMap.
                                entrySet()) {
                            String consumer = entry4.getKey();
                            Double potential = entry4.getValue();

//                            Add to potentials map
                            Integer key = (countryCode + custNo + custName
                                    + custGrp + consumer).hashCode();
//  Update potential
                            InstalledBaseBean v = potentialsMap.get(key);
                            v.setPotMaintenanceHrs(potential);

                        }
                    }
                }
            }

        }

//  Flatten the mtPotMap
        for (Map.Entry<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> entry : mtPotMap.
                entrySet()) {
            String countryCode = entry.getKey();
            Map<String, Map<String, Map<String, Map<String, Double>>>> custNoMap = entry.
                    getValue();
            for (Map.Entry<String, Map<String, Map<String, Map<String, Double>>>> entry1 : custNoMap.
                    entrySet()) {
                String custNo = entry1.getKey();
                Map<String, Map<String, Map<String, Double>>> custNameMap = entry1.
                        getValue();
                for (Map.Entry<String, Map<String, Map<String, Double>>> entry2 : custNameMap.
                        entrySet()) {
                    String custName = entry2.getKey();
                    Map<String, Map<String, Double>> custGrpMap = entry2.
                            getValue();
                    for (Map.Entry<String, Map<String, Double>> entry3 : custGrpMap.
                            entrySet()) {
                        String custGrp = entry3.getKey();
                        Map<String, Double> consumerMap = entry3.getValue();
                        for (Map.Entry<String, Double> entry4 : consumerMap.
                                entrySet()) {
                            String consumer = entry4.getKey();
                            Double potential = entry4.getValue();

//                            Add to potentials map
                            Integer key = (countryCode + custNo + custName
                                    + custGrp + consumer).hashCode();
//  Update potential
                            InstalledBaseBean v = potentialsMap.get(key);
                            v.setPotMaintenance(potential);

                        }
                    }
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
        System.out.println("Closed the driver in Potentials class.");
    }

}
