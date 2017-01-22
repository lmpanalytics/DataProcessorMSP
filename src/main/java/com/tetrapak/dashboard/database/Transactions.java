/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.database;

import com.tetrapak.dashboard.models.MarketBean;
import com.tetrapak.dashboard.models.MaterialBean;
import com.tetrapak.dashboard.models.TransactionBean;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

import static org.neo4j.driver.v1.Values.parameters;
import org.neo4j.driver.v1.exceptions.ClientException;

/**
 * This class runs queries against the Graph database
 *
 * @author SEPALMM
 */
public class Transactions {

    Driver driver = GraphDatabase.driver("bolt://localhost:7687",
            AuthTokens.basic("neo4j", "Tokyo2000"));

    public void testQuery() {
        try (Session session = driver.session()) {

            try (Transaction tx = session.beginTransaction()) {
                tx.run("CREATE (a:Person {name: {name}, title: {title}})",
                        parameters("name", "Arthur", "title", "King"));
                tx.success();
            }

            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run(
                        "MATCH (a:Person) WHERE a.name = {name} "
                        + "RETURN a.name AS name, a.title AS title",
                        parameters("name", "Arthur"));
                while (result.hasNext()) {
                    Record record = result.next();
                    System.out.println(String.format("%s %s", record.
                            get("title").asString(), record.get("name").
                            asString()));
                }
            }

        }

    }

    /**
     * Makes a Timeline Tree. Each year has its own set of month nodes; each
     * month has its own set of day nodes. The Cypher statement ensures that all
     * necessary nodes and relationships for a particular event—year, month,
     * day, plus the node representing the event itself—are either already
     * present in the graph, or, if not present, are added to the graph (MERGE
     * will add any missing elements):
     *
     * @param dateList the list of dates to use
     */
    public void makeTimeLineTree(List<LocalDate> dateList) {
        try (Session session = driver.session()) {
            String timelineID = "dashboard";
            timelineID = Utilities.toCypherVariableFormat(timelineID);

            for (LocalDate localDate : dateList) {
                int year = localDate.getYear();
                Month month = localDate.getMonth();
                int monthNumber = localDate.getMonth().getValue();
                String monthName = month.toString();
                int dayOfMonth = localDate.getDayOfMonth();
                DayOfWeek day = localDate.getDayOfWeek();
//                int dayOfWeek = day.getValue();

                try (Transaction tx1 = session.beginTransaction()) {
//              Run multiple statements
                    tx1.run("CREATE CONSTRAINT ON (t:Timeline)"
                            + " ASSERT t.id IS UNIQUE");
                    tx1.run("CREATE INDEX ON :Year(year)");
                    tx1.run("CREATE INDEX ON :Month(month)");
                    tx1.run("CREATE INDEX ON :Day(dayOfMonth)");

                    tx1.success();
                }

                String tx2 = "MERGE (t:Timeline { id:" + timelineID + "})"
                        + " MERGE ((t)-[:YEAR]->(y:Year { year:" + year + "}))"
                        + " MERGE ((y)-[:MONTH]->(m:Month {month:" + monthNumber + ", name:'" + monthName + "'}))"
                        + " MERGE ((m)-[:DAY]->(d:Day { year:" + year + ", month:" + monthNumber + ", dayOfMonth:" + dayOfMonth + ", name:'" + day + "'}))";

//                System.out.println("Preparing transaction " + tx2);
                session.run(tx2);

            }

        } catch (ClientException e) {
            System.err.println("Exception in makeTimeLineTree:" + e);
        }
    }

    /**
     * Loads and creates Nodes for Clusters, Market Groups, and Markets. An
     * exception is thrown and the program exits in case the value-data change
     * in the Market Map vs. the database content.
     *
     * @param marketMap containing the Market-Key, and Values of Market-Number,
     * -Name, -Group, and -Cluster
     */
    public void loadMarketData(Map<String, MarketBean> marketMap) {
        try (Session session = driver.session()) {

            int transactionCounter = 0;
            boolean setIndex = true;
            String globalMktID = "dashboard";
            globalMktID = Utilities.toCypherVariableFormat(globalMktID);

            for (Map.Entry<String, MarketBean> entry : marketMap.entrySet()) {
                String key = entry.getKey();
                MarketBean value = entry.getValue();

                String mktNumber = key;
                String mktName = value.getMarketName();
                String mktGroup = value.getMarketGroup();
                String cluster = value.getCluster();
//                System.out.println(mktNumber + "; " + mktName + "; " + mktGroup + "; " + cluster);

                while (setIndex) {
                    try (Transaction tx1 = session.beginTransaction()) {
//              Run multiple statements
                        tx1.run("CREATE CONSTRAINT ON (mkt:Market)"
                                + " ASSERT mkt.id IS UNIQUE");
                        tx1.run("CREATE INDEX ON :Cluster(name)");
                        tx1.run("CREATE INDEX ON :Market(name)");

                        tx1.success();
                        setIndex = false;
                    }
                }

                String tx2 = "MERGE (g:GlobalMkt { id:" + globalMktID + ", name:'GLOBAL MARKET'})"
                        + " MERGE ((g)-[:CLUSTER]->(c:Cluster { name:'" + cluster + "'}))"
                        + " MERGE ((c)-[:MARKETGROUP]->(mgrp:MarketGroup { name:'" + mktGroup + "'}))"
                        + " MERGE ((mgrp)-[:MARKET]->(mkt:Market { id:'" + mktNumber + "', name:'" + mktName + "'}))";

//                System.out.println("Preparing transaction " + tx2);
                session.run(tx2);

                transactionCounter++;
            }
            System.out.format("Processed %d markets.\n", transactionCounter);
        } catch (ClientException e) {
            System.err.println("Exception in loadMarketData:" + e);
            System.exit(1);
        }

    }

    /**
     * Loads and creates Nodes for Assortment Groups, MPGs, Material Names, and
     * Material Numbers. An exception is thrown and the program exits in case
     * the value-data change in the Material Map vs. the database content.
     *
     * @param materialMap containing the Material-Key, and Values of
     * Material-Number, -Name, MPG, and Assortment Group
     */
    public void loadMaterialData(Map<String, MaterialBean> materialMap) {
        try (Session session = driver.session()) {

            int transactionCounter = 0;
            boolean setIndex = true;

            String globalMtrlID = "dashboard";
            globalMtrlID = Utilities.toCypherVariableFormat(globalMtrlID);

            for (Map.Entry<String, MaterialBean> entry : materialMap.entrySet()) {
                String key = entry.getKey();
                MaterialBean value = entry.getValue();

                String mtrlNumber = key;
//                Fix read error by removing "'" from material names
                String mtrlName = value.getMaterialName().replaceAll("'",
                        "");
                String mpg = value.getMpg();
                String assortment = value.getAssortmentGroup();

                while (setIndex) {
                    try (Transaction tx1 = session.beginTransaction()) {
//              Run multiple statements
                        tx1.run("CREATE CONSTRAINT ON (mtrl:Material)"
                                + " ASSERT mtrl.id IS UNIQUE");
                        tx1.run("CREATE INDEX ON :Assortment(name)");
                        tx1.run("CREATE INDEX ON :Mpg(name)");

                        tx1.success();
                        setIndex = false;
                    }
                }

                String tx2 = "MERGE (g:GlobalMtrl { id:" + globalMtrlID + ", name:'GLOBAL MATERIAL'})"
                        + " MERGE ((g)-[:ASSORTMENT]->(a:Assortment { name:'" + assortment + "'}))"
                        + " MERGE ((a)-[:MPG]->(mpg:Mpg { name:'" + mpg + "'}))"
                        + " MERGE ((mpg)-[:MATERIAL]->(mtrl:Material { id:'" + mtrlNumber + "', name:'" + mtrlName + "'}))";

//                System.out.println("Preparing transaction " + tx2);
                session.run(tx2);

                transactionCounter++;
            }
            System.out.format("Processed %d materials.\n", transactionCounter);
        } catch (ClientException e) {
            System.err.println("Exception in loadMaterialData:" + e);
            System.exit(2);
        }

    }

    /**
     * Loads and creates Nodes for Customer data: Final Customer Number, Final
     * Customer Name, Customer Group, and Customer Type.
     *
     * @param transactionMap
     */
    public void loadCustomerData(Map<Integer, TransactionBean> transactionMap) {
//        Collect unique customer data in a map from the larger transaction map

        Map<Integer, List<String>> customerMap = new HashMap<>();
        List<String> customerData = null;

        for (Map.Entry<Integer, TransactionBean> entry : transactionMap.
                entrySet()) {
            TransactionBean value = entry.getValue();

            String customerNumber = value.getFinalCustomerKey();
//                Fix read error by removing "'" from customer names
            String customerName = value.getFinalCustomerName().replaceAll(
                    "'", "");
            String customerGroup = value.getCustomerGroup();
            String customerType = value.getCustomerType();

            customerData = new ArrayList<>();
            customerData.add(customerNumber);
            customerData.add(customerName);
            customerData.add(customerGroup);
            customerData.add(customerType);
            String custKey = customerNumber + customerName + customerGroup + customerType;
            customerMap.put(custKey.hashCode(), customerData);
        }

//            Add customer data to Neo4j
        try (Session session = driver.session()) {

            int transactionCounter = 0;
            boolean setIndex = true;

            for (Map.Entry<Integer, List<String>> entry : customerMap.
                    entrySet()) {
                Integer key = entry.getKey();
                List<String> value = entry.getValue();

                while (setIndex) {
                    try (Transaction tx1 = session.beginTransaction()) {
//              Run multiple statements
                        tx1.run("CREATE CONSTRAINT ON (c:Customer)"
                                + " ASSERT c.id IS UNIQUE");

                        tx1.success();
                        setIndex = false;
                    }
                }

                String customerNumber = value.get(0);
                String customerName = value.get(1);
                String customerGroup = value.get(2);
                String customerType = value.get(3);

                String tx2 = "MERGE (c:Customer{id:'" + customerNumber + "',name:'" + customerName + "',custGroup:'" + customerGroup + "',custType:'" + customerType + "'})";

//                System.out.println("Preparing transaction " + tx2);
                session.run(tx2);

                transactionCounter++;
            }
            System.out.
                    format("Processed %d unique customers.\n",
                            transactionCounter);
        } catch (ClientException e) {
            System.err.println("Exception in loadCustomerData:" + e);
        }

    }

    /**
     * Creates Relationships between sales transaction data: Date, Service
     * Category (Finance), Market Number, Final Customer Number, and Material
     * Number.
     *
     * @param transactionMap
     */
    public void loadTransactionData(Map<Integer, TransactionBean> transactionMap) {
        try (Session session = driver.session()) {

            int transactionCounter = 0;

            for (Map.Entry<Integer, TransactionBean> entry : transactionMap.
                    entrySet()) {
//                Integer key = entry.getKey();
                TransactionBean value = entry.getValue();

                LocalDate date = value.getDate();
                int year = date.getYear();
                int month = date.getMonthValue();
                int day = date.getDayOfMonth();
                String category = value.getCategory();
                String marketNumber = value.getMarketKey();
                String customerNumber = value.getFinalCustomerKey();
                String materialNumber = value.getMaterialKey();
                Double netSales = value.getNetSales();
                Double directCost = value.getDirectCost();
                Double quantity = value.getInvoiceQuantity();

                String tx2 = "MATCH (d:Day {year: " + year + ", month: " + month + ", dayOfMonth: " + day + "})"
                        + " MATCH (mtr:Material { id:'" + materialNumber + "'})"
                        + " MATCH (fc:Customer {id: '" + customerNumber + "'})"
                        + " MATCH (cat:ServiceCategory {name: '" + category + "'})"
                        + " MATCH (mkt:Market {id: '" + marketNumber + "'})"
                        + " MERGE ((d)<-[:SOLD_ON {netSales: " + netSales + ", directCost: " + directCost + ", quantity: " + quantity + "}]-(mtr))"
                        + " MERGE ((mtr)-[:SOLD_TO]->(fc))"
                        + " MERGE ((mtr)-[:CATEGORY]->(cat))"
                        + " MERGE ((mtr)-[:SOLD_IN]->(mkt))";

//                System.out.println("Preparing transaction " + tx3);
                session.run(tx2);
                transactionCounter++;
            }
            System.out.
                    format("Processed %d transactions.\n",
                            transactionCounter);
        } catch (ClientException e) {
            System.err.println("Exception in loadTransactionData:" + e);
        }

    }

    /**
     * Create Nodes for Service Categories 'Maintenance Work' and 'Parts'
     *
     */
    public void createServiceCategories() {
        try (Session session = driver.session()) {
            boolean setIndex = true;

            while (setIndex) {
                try (Transaction tx1 = session.beginTransaction()) {
//              Run multiple statements
                    tx1.run("CREATE CONSTRAINT ON (s:ServiceCategory)"
                            + " ASSERT s.name IS UNIQUE");

                    tx1.success();
                    setIndex = false;
                }
            }

            String tx2 = "MERGE (:ServiceCategory { name:'Maintenance Work'})"
                    + " MERGE (:ServiceCategory { name:'Parts'})";

//                System.out.println("Preparing transaction " + tx2);
            session.run(tx2);

            System.out.println(
                    "Assured presence of Service Categories 'Maintenance Work' and 'Parts'.");
        } catch (ClientException e) {
            System.err.println("Exception in createServiceCategories:" + e);
        }

    }

    /**
     * Close the DB driver
     */
    public void closeNeo4jDriver() {
        driver.close();
        System.out.println("Closed the driver.");
    }
}
