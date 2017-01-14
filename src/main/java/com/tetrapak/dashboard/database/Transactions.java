/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.database;

import com.tetrapak.dashboard.models.MarketBean;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
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
     * Loads and creates Nodes for Clusters, Market Groups, and Markets
     *
     * @param marketMap containing the Market-Key, and Values of Market-Number
     * -Name, -Group, and -Cluster
     */
    public void loadMarketData(Map<String, MarketBean> marketMap) {
        try (Session session = driver.session()) {

            int transactionCounter = 0;

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

                try (Transaction tx1 = session.beginTransaction()) {
//              Run multiple statements
                    tx1.run("CREATE CONSTRAINT ON (mkt:Market)"
                            + " ASSERT mkt.id IS UNIQUE");
                    tx1.run("CREATE INDEX ON :Market(mktName)");

                    tx1.success();
                }

                String tx2 = "MERGE (g:GlobalMkt { id:" + globalMktID + ", name:'GLOBAL MARKET'})"
                        + " MERGE ((g)-[:CLUSTER]->(c:Cluster { cluster:'" + cluster + "'}))"
                        + " MERGE ((c)-[:MARKETGROUP]->(mgrp:MarketGroup { marketGroup:'" + mktGroup + "'}))"
                        + " MERGE ((mgrp)-[:MARKET]->(mkt:Market { id:'" + mktNumber + "', name:'" + mktName + "'}))";

//                System.out.println("Preparing transaction " + tx2);
                session.run(tx2);

                transactionCounter++;
            }
            System.out.format("Processed %d markets.\n", transactionCounter);
        } catch (ClientException e) {
            System.err.println("Exception in loadMarketData:" + e);
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
