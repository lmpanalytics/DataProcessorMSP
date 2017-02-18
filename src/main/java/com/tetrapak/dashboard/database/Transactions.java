/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.database;

import com.tetrapak.dashboard.dataprocessor.MarketMaker;
import com.tetrapak.dashboard.models.InstalledBaseBean;
import com.tetrapak.dashboard.models.InvoiceBean;
import com.tetrapak.dashboard.models.MarketBean;
import com.tetrapak.dashboard.models.MaterialBean;
import com.tetrapak.dashboard.models.ReferencePartBean;
import com.tetrapak.dashboard.models.TransactionBean;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Values;

import org.neo4j.driver.v1.exceptions.ClientException;

/**
 * This class runs queries against the Graph database related to sales
 * transactions.
 *
 * @author SEPALMM
 */
public class Transactions {

    Driver driver = GraphDatabase.driver(Utilities.URI(),
            AuthTokens.basic(Utilities.myUserName(), Utilities.myPassword()));

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

            for (LocalDate localDate : dateList) {
                int year = localDate.getYear();
                Month month = localDate.getMonth();
                int monthNumber = localDate.getMonth().getValue();
                String monthName = month.toString();
                int dayOfMonth = localDate.getDayOfMonth();
                String day = localDate.getDayOfWeek().toString();

                try (Transaction tx1 = session.beginTransaction()) {
//              Run multiple statements
                    tx1.run("CREATE CONSTRAINT ON (t:Timeline)"
                            + " ASSERT t.id IS UNIQUE");
                    tx1.run("CREATE INDEX ON :Year(year)");
                    tx1.run("CREATE INDEX ON :Month(month)");
                    tx1.run("CREATE INDEX ON :Day(dayOfMonth)");

                    tx1.success();
                }

                String tx2 = "MERGE (t:Timeline {id: {timelineID}})"
                        + " MERGE ((t)-[:YEAR]->(y:Year { year: {year}}))"
                        + " MERGE ((y)-[:MONTH]->(m:Month {month: {monthNumber}, name: {monthName}}))"
                        + " MERGE ((m)-[:DAY]->(d:Day { year: {year}, month: {monthNumber}, dayOfMonth: {dayOfMonth}, name: {day}}))";

                session.run(tx2, Values.parameters(
                        "timelineID", timelineID,
                        "year", year,
                        "monthNumber", monthNumber,
                        "monthName", monthName,
                        "dayOfMonth", dayOfMonth,
                        "day", day
                ));

            }

        } catch (ClientException e) {
            System.err.
                    println("Exception in makeTimeLineTree:" + e.getMessage());
        }
    }

    /**
     * Creates Nodes for Clusters, Market Groups, Markets and Countries. The
     * geographical structure is hard-coded in class MarketMaker.
     *
     * @param marketMap containing the Country-Key, and Values of Country ISO
     * code, Country Name, Market-Number, -Name, -Group, and Cluster
     */
    public void loadMarketData(Map<String, MarketBean> marketMap) {
        try (Session session = driver.session()) {

            int transactionCounter = 0;
            boolean setIndex = true;
            String globalMktID = "dashboard";

            for (Map.Entry<String, MarketBean> entry : marketMap.entrySet()) {
                String key = entry.getKey();
                MarketBean value = entry.getValue();

                String isoCountryCode = key;
                String countryName = value.getCountryISOname();
                String mktCode = value.getMarketKey();
                String mktName = value.getMarketName();
                String mktGroupCode = value.getMarketGroupKey();
                String mktGroupName = value.getMarketGroupName();
                String cluster = value.getCluster();

//                System.out.printf("%s, %s, %s, %s, %s, %s, %s\n", key, countryName, mktCode, mktName, mktGroupCode, mktGroupName, cluster);
                while (setIndex) {
                    try (Transaction tx1 = session.beginTransaction()) {
//              Run multiple statements
                        tx1.run("CREATE CONSTRAINT ON (cy:Country)"
                                + " ASSERT cy.countryId IS UNIQUE");
                        tx1.run("CREATE INDEX ON :Cluster(name)");
                        tx1.run("CREATE INDEX ON :MarketGroup(mktGroupCode)");
                        tx1.run("CREATE INDEX ON :Market(mktCode)");

                        tx1.success();
                        setIndex = false;
                    }
                }

                String tx2 = "MERGE (g:GlobalMkt { id: {globalMktID}, name:'GLOBAL MARKET'})"
                        + " MERGE ((g)-[:CLUSTER]->(c:Cluster { name:{cluster}}))"
                        + " MERGE ((c)-[:MARKETGROUP]->(mgrp:MarketGroup {id: {mktGroupCode}, name: {mktGroupName}}))"
                        + " MERGE ((mgrp)-[:MARKET]->(mkt:Market:Country {mktId: {mktCode}, mktName: {mktName}, countryId: {isoCountryCode}, countryName: {countryName}}))";

                session.run(tx2, Values.parameters(
                        "globalMktID", globalMktID,
                        "cluster", cluster,
                        "mktGroupCode", mktGroupCode,
                        "mktGroupName", mktGroupName,
                        "mktCode", mktCode,
                        "mktName", mktName,
                        "isoCountryCode", isoCountryCode,
                        "countryName", countryName
                ));

                transactionCounter++;
            }
            System.out.format("Processed %d markets.\n", transactionCounter);
        } catch (ClientException e) {
            System.err.println("Exception in loadMarketData:" + e.getMessage());
            System.exit(1);
        }

    }

    /**
     * Loads and creates Nodes for Assortment Groups, MPGs, Material Names, and
     * Material Numbers. An exception is thrown and the program exits in case
     * the value-data change in the Material Map vs. the database content.
     *
     * Information regarding the grouping of materials according to reference
     * parts is added to the Material nodes.
     *
     * @param materialMap containing the Material-Key, and Values of
     * Material-Number, -Name, MPG, and Assortment Group
     * @param refMtrlMap containing the Reference Material-Key, and Values of
     * Reference Material-Number, and -Name.
     */
    public void loadMaterialData(Map<String, MaterialBean> materialMap,
            Map<String, ReferencePartBean> refMtrlMap) {
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

//                Lookup and assign the refence material name
                String refName = "";
                if (refMtrlMap.containsKey(mtrlNumber)) {
                    refName = refMtrlMap.get(mtrlNumber).getRefMaterialName();
                }

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

                String tx2 = "MERGE (g:GlobalMtrl { id: {globalMtrlID}, name:'GLOBAL MATERIAL'})"
                        + " MERGE ((g)-[:ASSORTMENT]->(a:Assortment { name: {assortment}}))"
                        + " MERGE ((a)-[:MPG]->(mpg:Mpg { name: {mpg}}))"
                        + " MERGE ((mpg)-[:MATERIAL]->(mtrl:Material { id: {mtrlNumber}, name: {mtrlName}, refMtrlName: {refName}}))";

                session.run(tx2, Values.parameters(
                        "globalMtrlID", globalMtrlID,
                        "assortment", assortment,
                        "mpg", mpg,
                        "mtrlNumber", mtrlNumber,
                        "mtrlName", mtrlName,
                        "refName", refName
                ));

                transactionCounter++;
            }
            System.out.format("Processed %d materials.\n", transactionCounter);
        } catch (ClientException e) {
            System.err.
                    println("Exception in loadMaterialData:" + e.getMessage());
            System.exit(2);
        }

    }

    /**
     * Loads and creates Nodes for Customer data: Final Customer Number, Final
     * Customer Name, Customer Group, and Customer Type. An exception is thrown
     * and the program exits in case the value-data change in the Transaction
     * Map or Installed Base Map vs. the database content. Based on Customer
     * Groups, three different Customer types are assigned: 'Global Account',
     * 'Int. Account', and the rest as type 'Other'.
     *
     * Basic flow: 1) Query database and collect current customer data
     * originating from BO. 2) Collect unique customer data from the BO
     * transaction map and add to customerMap if absent. 3) Delete Customers in
     * DB originating from TecBase. 4) Add Customer data from TecBase to
     * customerMap if absent. 5) Add customer data to database.
     *
     * This ensures that Customer data from BO Special Ledger report has
     * precedence over Installed Base data. It also means that earlier data has
     * precedence over newer data. - Try to change this in a future fix so that
     * newer data decides the customer group membership in the data base.
     *
     * @param transactionMap Special Ledger data
     * @param installedBaseMap TecBase data
     */
    public void loadCustomerData(Map<Integer, TransactionBean> transactionMap,
            Map<Integer, InstalledBaseBean> installedBaseMap) {

//      Initialize maps and lists
        Map<Integer, List<String>> customerMap = new HashMap<>();
        List<String> customerData = null;

//        Query database and collect current customer data originating from BO
        try (Session session = driver.session()) {

            String tx = "MATCH (c:Customer {isSourceBO: 'true'})"
                    + " RETURN c.id AS ID, c.name AS Name, c.custGroup AS CustGroup, c.custType AS CustType, c.isSourceBO AS IsSourceBO";

            StatementResult result = session.run(tx);

            while (result.hasNext()) {
                Record r = result.next();

                String customerNumber = r.get("ID").asString();
                String customerName = r.get("Name").asString();
                String customerGroup = r.get("CustGroup").asString();
                String customerType = r.get("CustType").asString();
                String isSourceBO = r.get("IsSourceBO").asString();

//            Add results to Map
                customerData = new ArrayList<>();
                customerData.add(customerNumber);
                customerData.add(customerName);
                customerData.add(customerGroup);
                customerData.add(customerType);
                customerData.add(isSourceBO);
                String custKey = customerNumber;
//                Add results to map if originating from BO
                if (isSourceBO.equals("true")) {
                    customerMap.put(custKey.hashCode(), customerData);
//                    System.out.printf("Originating from BO, and add to customer Map: %s, %s\n", customerNumber, customerName);
                }

            }

        } catch (ClientException e) {
            System.err.println("Exception in 'populateSalesMap()':" + e);
        }

//        Collect unique customer data from the BO transaction map and add to customerMap if absent
        for (Map.Entry<Integer, TransactionBean> entry : transactionMap.
                entrySet()) {
            TransactionBean value = entry.getValue();

            String customerNumber = value.getFinalCustomerKey();
//                Fix read error by removing "'" from customer names
            String customerName = value.getFinalCustomerName().replaceAll(
                    "'", "");
            String customerGroup = value.getCustomerGroup();
            String customerType = Utilities.makeCustType(customerGroup);

            customerData = new ArrayList<>();
            customerData.add(customerNumber);
            customerData.add(customerName);
            customerData.add(customerGroup);
            customerData.add(customerType);
            customerData.add(value.isSourceBO().toString());
            String custKey = customerNumber;
            customerMap.putIfAbsent(custKey.hashCode(), customerData);
        }

//        Delete Customers in DB originating from TecBase
        try (Session session = driver.session()) {
            Transaction tx1 = session.beginTransaction();
            tx1.run("MATCH (c:Customer {isSourceBO: 'false'}) DETACH DELETE c");
            tx1.success();
        }

        //        Add Customer data from TecBase to customerMap if absent
        for (Map.Entry<Integer, InstalledBaseBean> entry : installedBaseMap.
                entrySet()) {
            InstalledBaseBean value = entry.getValue();

            String customerNumber = value.getFinalCustomerKey();
//                Fix read error by removing "'" from customer names
            String customerName = value.getFinalCustomerName().replaceAll(
                    "'", "");
            String customerGroup = value.getCustomerGroup();
            String customerType = Utilities.makeCustType(customerGroup);
            String isSourceBO = "false";

            customerData = new ArrayList<>();
            customerData.add(customerNumber);
            customerData.add(customerName);
            customerData.add(customerGroup);
            customerData.add(customerType);
            customerData.add(isSourceBO);
            String custKey = customerNumber;
            customerMap.putIfAbsent(custKey.hashCode(), customerData);
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
                String isSourceBO = value.get(4);

                String tx3 = "MERGE (c:Customer{id: {customerNumber}, name: {customerName}, custGroup: {customerGroup}, custType: {customerType}, isSourceBO: {isSourceBO}})";

                session.run(tx3, Values.parameters(
                        "customerNumber", customerNumber,
                        "customerName", customerName,
                        "customerGroup", customerGroup,
                        "customerType", customerType,
                        "isSourceBO", isSourceBO
                ));

                transactionCounter++;
            }
            System.out.format("Processed %d unique customers.\n",
                    transactionCounter);
        } catch (ClientException e) {
            System.err.
                    println("Exception in loadCustomerData:" + e.getMessage());
            System.exit(3);
        }

    }

    /**
     * Creates Relationships between sales transaction data: Date, Service
     * Category (Finance), Market Number, Final Customer Number, and Material
     * Number.
     *
     * @param transactionMap contains values from the Special Ledger Report.
     * @param invoiceMap containing the Market and Material Composite Key, and
     * Values of MarketKey Material Number, Assortment Group, and MPG.
     * @param materialMap containing the Material Number Key, and values of
     * Material-Number, -Name, MPG, and the Assortment Group.
     */
    public void loadTransactionData(Map<Integer, TransactionBean> transactionMap,
            Map<Integer, InvoiceBean> invoiceMap,
            Map<String, MaterialBean> materialMap) {

        try (Session session = driver.session()) {

            /* =========== TEST FOR NEW MARKETS START ===========*/
            //       Make set of existing hardcoded markets
            Set<String> existingMarkets = new HashSet<>();
            Map<String, MarketBean> marketMap = MarketMaker.getMarketMap();
            marketMap.entrySet().stream().
                    map((entry) -> entry.getValue()).
                    forEachOrdered((value) -> {
                        existingMarkets.add(value.getMarketKey());
                    });

//       Make set of markets in added sales transactions
            Set<String> marketsInTransactions = new HashSet<>();
            transactionMap.entrySet().stream().
                    map((entry) -> entry.getValue()).
                    forEachOrdered((value) -> {
                        marketsInTransactions.add(value.getMarketKey());
                    });

//       If sets are not identical, print diff and exit program
            if (!existingMarkets.containsAll(marketsInTransactions)) {
                System.err.println(
                        "ERROR >> Found new markets in sales transactions:");
                Set<String> difference = new HashSet<>(marketsInTransactions);
                difference.removeAll(existingMarkets);
                difference.forEach((diff) -> {
                    System.err.printf("New market number: %s\n", diff);
                });
                System.err.println(
                        "Manually add these to class MarketMaker before proceeding. Program exits.");
                System.exit(4);
            }
            /* =========== TEST FOR NEW MARKETS END ===========*/

            int transactionCounter = 0;

            for (Map.Entry<Integer, TransactionBean> entry : transactionMap.
                    entrySet()) {
//                Integer key = entry.getKey();
                TransactionBean value = entry.getValue();
                String localAssortment = "";
                String localMPG = "";

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

//                Fix Blank Assortment Groups
                if (materialMap.containsKey(materialNumber)) {
//              Look up assortment group
                    String globalAssortmentGrp = materialMap.get(materialNumber).
                            getAssortmentGroup();
                    if (globalAssortmentGrp.equals("Blank assortment group")) {

                        /* Look up the assortment group used by the corresponding 
                    transaction market for this material number. 
                    This information is in the invoice map. */
                        Integer compKey = (marketNumber + materialNumber).
                                hashCode();

//                    Handle null pointer exception
                        if (invoiceMap.containsKey(compKey)) {
                            String lookupLocalAssortmentGrp = invoiceMap.
                                    get(compKey).
                                    getAssortmentGroup();

                            /*  If the local assortment group is not "Blank", update 
                    Local Assortment Group and MPG in the 'SOLD_ON' relationship. */
                            if (!lookupLocalAssortmentGrp.equals(
                                    "Blank assortment group")) {
                                localAssortment = lookupLocalAssortmentGrp;
                                localMPG = invoiceMap.get(compKey).getMpg();
//                        System.out.printf("Re-assign %s of mtrl %s to %s and MPG %s\n", globalAssortmentGrp, materialNumber, localAssortment, localMPG);
                            }
                        }
                    }
                } else {
                    System.err.println(
                            ">> WARNING: Global materialMap is missing material: " + materialNumber + "");
                }

                String tx2 = "MATCH (d:Day {year: {year}, month: {month}, dayOfMonth: {day}})"
                        + " MATCH (mtr:Material { id: {materialNumber}})"
                        + " MATCH (fc:Customer {id: {customerNumber}})"
                        + " MATCH (cat:ServiceCategory {name: {category}})"
                        + " MATCH (mkt:Market {mktId: {marketNumber}})"
                        + " MERGE ((d)<-[:SOLD_ON {custNumber: {customerNumber}, marketNumber: {marketNumber}, localAssortmentGrp: {localAssortment}, localMPG: {localMPG}, netSales: {netSales}, directCost: {directCost}, quantity: {quantity}}]-(mtr))"
                        + " MERGE ((mtr)-[:FOR_FINAL_CUSTOMER]->(fc))"
                        + " MERGE ((mtr)-[:OF_CATEGORY]->(cat))"
                        + " MERGE ((mtr)-[:SOLD_FROM]->(mkt))"
                        + " MERGE ((fc)-[:LOCATED_IN]->(mkt))";

                session.run(tx2, Values.parameters(
                        "year", year,
                        "month", month,
                        "day", day,
                        "materialNumber", materialNumber,
                        "customerNumber", customerNumber,
                        "category", category,
                        "marketNumber", marketNumber,
                        "localAssortment", localAssortment,
                        "localMPG", localMPG,
                        "netSales", netSales,
                        "directCost", directCost,
                        "quantity", quantity
                ));
                transactionCounter++;
            }
            System.out.format("Processed %d sales transactions.\n",
                    transactionCounter);
        } catch (ClientException e) {
            System.err.println("Exception in loadTransactionData:" + e.
                    getMessage());
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
            System.err.println("Exception in createServiceCategories:" + e.
                    getMessage());
        }

    }

    /**
     * Close the DB driver
     */
    public void closeNeo4jDriver() {
        driver.close();
        System.out.println("Closed the driver in Transactions class.");
    }
}
