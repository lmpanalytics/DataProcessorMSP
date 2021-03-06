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
import java.sql.Timestamp;
import java.time.LocalDate;
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
                        tx1.run("CREATE CONSTRAINT ON (cy:CountryDB)"
                                + " ASSERT cy.countryId IS UNIQUE");
                        tx1.run("CREATE CONSTRAINT ON (c:ClusterDB)"
                                + " ASSERT c.name IS UNIQUE");
                        tx1.run("CREATE CONSTRAINT ON (mg:MarketGroup)"
                                + " ASSERT mg.mktGrpId IS UNIQUE");

                        tx1.run("CREATE INDEX ON :MarketGroup(name)");
                        tx1.run("CREATE INDEX ON :MarketDB(mktId)");
                        tx1.run("CREATE INDEX ON :MarketDB(mktName)");
                        tx1.run("CREATE INDEX ON :CountryDB(countryName)");

                        tx1.success();
                        setIndex = false;
                    }
                }

                String tx2 = "MERGE (c:ClusterDB { name:{cluster}})"
                        + " MERGE (mgrp:MarketGroup {mktGrpId: {mktGroupCode}, name: {mktGroupName}})"
                        + " MERGE (mkt:MarketDB:CountryDB {mktId: {mktCode}, mktName: {mktName}, countryId: {isoCountryCode}, countryName: {countryName}})"
                        + " MERGE (c)<-[:MEMBER_OF]-(mgrp)"
                        + " MERGE (mgrp)<-[:MEMBER_OF]-(mkt)";

                session.run(tx2, Values.parameters(
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
            String customerName = Utilities.cleanCustomerName(
                    value.getFinalCustomerName());
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
            System.out.println("Preparing to delete customers");
            Transaction tx1 = session.beginTransaction();
            tx1.run("MATCH (c:Customer {isSourceBO: 'false'}) DETACH DELETE c");
            tx1.success();
        } catch (Exception e) {
            System.err.println("Exception in deleting Customers in DB originating from TecBase. Program exits. " + e.getMessage());
            System.exit(2);
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
                        tx1.run("CREATE INDEX ON :Customer(isSourceBO)");
                        tx1.run("CREATE INDEX ON :Customer(custType)");
                        tx1.run("CREATE INDEX ON :Customer(name)");
                        tx1.run("CREATE INDEX ON :Customer(custGroup)");

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
     * Creates the Sales Transaction Nodes and Relationships between sales
     * transaction data: Service Category (Finance), Market Number, Final
     * Customer Number, Assortment, MPG, and Reference Parts.
     *
     * @param transactionMap contains values from the Special Ledger Report.
     * @param invoiceMap containing the Market and Material Composite Key, and
     * Values of MarketKey Material Number, Assortment Group, and MPG.
     * @param materialMap containing the Material Number Key, and values of
     * Material-Number, -Name, MPG, and the Assortment Group.
     * @param refMtrlMap containing the Reference Material Key, and value of the
     * Reference material name
     */
    public void loadTransactionData(
            Map<Integer, TransactionBean> transactionMap,
            Map<Integer, InvoiceBean> invoiceMap,
            Map<String, MaterialBean> materialMap,
            Map<String, ReferencePartBean> refMtrlMap) {

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
                Integer key = entry.getKey();
                TransactionBean value = entry.getValue();
//                Initialize variables
                boolean setIndex = true;
                String referenceMtrlName = "Other";
                String globalAssortmentGrp = "Blank assortment group";
                String globalMpg = "Not assigned";

//              Get data
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

//                Reduce 'Blank Assortment Groups', 'Vacant' and 'Other parts'
                if (materialMap.containsKey(materialNumber)) {
//              Look up assortment group
                    globalAssortmentGrp = materialMap.get(materialNumber).
                            getAssortmentGroup();
                    globalMpg = materialMap.get(materialNumber).getMpg();
                    if (globalAssortmentGrp.equals("Blank assortment group")
                            || globalAssortmentGrp.equals("Vacant")
                            || globalAssortmentGrp.equals("Other parts")) {

                        /* Look up the assortment group used by the corresponding 
                    transaction market for this material number. 
                    This information is in the invoice map. */
                        Integer compKey = (marketNumber + materialNumber).
                                hashCode();

//                    Handle null pointer exception
                        if (invoiceMap.containsKey(compKey)) {
                            String lookupLocalAssortmentGrp = invoiceMap.
                                    get(compKey).getAssortmentGroup();

                            /*  If the local assortment group is not "Blank", 
                            "Vacant" and "Other parts" use the local lookup 
                            value for the Global Assortment Group and MPG. */
                            if (!lookupLocalAssortmentGrp.
                                    equals("Blank assortment group")
                                    && !lookupLocalAssortmentGrp.
                                            equals("Vacant")
                                    && !lookupLocalAssortmentGrp.
                                            equals("Other parts")) {
                                globalAssortmentGrp = lookupLocalAssortmentGrp;
                                globalMpg = invoiceMap.get(compKey).getMpg();
//                        System.out.printf("Re-assign mtrl %s to Assortment %s and MPG %s\n", materialNumber, globalAssortmentGrp, globalMpg);
                            }
                        }
                    }
                } else {
//          Material is missing as it is cancelled out in a reverse transaction
                    System.err.println(
                            ">> WARNING: Global materialMap is missing material: " + materialNumber + "");
                }

                /* Look up the reference material, if any, mapped to the corresponding 
                     material number. This information is in the refMtrlMap. */
                //                    Handle null pointer exception
                if (refMtrlMap.containsKey(materialNumber)) {
                    referenceMtrlName = refMtrlMap.get(materialNumber).
                            getRefMaterialName();
                }

                while (setIndex) {
                    try (Transaction tx1 = session.beginTransaction()) {
//              Run multiple statements
                        tx1.run("CREATE CONSTRAINT ON (t:Transaction)"
                                + " ASSERT t.trId IS UNIQUE");
                        tx1.run("CREATE CONSTRAINT ON (a:Assortment)"
                                + " ASSERT a.name IS UNIQUE");
                        tx1.run("CREATE CONSTRAINT ON (mpg:Mpg)"
                                + " ASSERT mpg.name IS UNIQUE");
                        tx1.run("CREATE CONSTRAINT ON (ref:RefMaterial)"
                                + " ASSERT ref.refMtrlName IS UNIQUE");

                        tx1.run("CREATE INDEX ON :Transaction(dayOfMonth)");
                        tx1.run("CREATE INDEX ON :Transaction(month)");
                        tx1.run("CREATE INDEX ON :Transaction(year)");

                        tx1.success();
                        setIndex = false;
                    }
                }

                String tx2 = "MATCH (fc:Customer {id: {customerNumber}}),"
                        + " (cat:ServiceCategory {name: {category}}),"
                        + " (mkt:MarketDB {mktId: {marketNumber}})"
                        /* Create Reference materials, Assortments and MPGs */
                        + " MERGE (ref:RefMaterial {refMtrlName: {refMtrlName}})"
                        + " MERGE (a:Assortment {name: {globalAssortmentGrp}})"
                        + " MERGE (mpg:Mpg {name: {globalMpg}})"
                        /* Create Transactions and relationships */
                        + " MERGE (t:Transaction {trId: {trId}, year: {year}, month: {month}, dayOfMonth: {day}})"
                        + " MERGE (t)-[:FOR {netSales: {netSales}, directCost: {directCost}, quantity: {quantity}}]->(fc)"
                        + " MERGE (a)-[:IN]->(t)"
                        + " MERGE (mpg)-[:IN]->(t)"
                        + " MERGE (ref)-[:IN]->(t)"
                        + " MERGE (t)-[:BOOKED_AS]->(cat)"
                        + " MERGE (mkt)-[:MADE]->(t)";

                session.run(tx2, Values.parameters(
                        "refMtrlName", referenceMtrlName,
                        "globalAssortmentGrp", globalAssortmentGrp,
                        "globalMpg", globalMpg,
                        "customerNumber", customerNumber,
                        "category", category,
                        "marketNumber", marketNumber,
                        "trId", key,
                        "year", year,
                        "month", month,
                        "day", day,
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
        Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
        System.out.println(
                timestampEnd + " :: Closed the driver in Transactions class.");
    }
}
