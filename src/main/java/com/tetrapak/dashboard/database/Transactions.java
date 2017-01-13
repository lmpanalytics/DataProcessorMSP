/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.database;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

import static org.neo4j.driver.v1.Values.parameters;

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

    public void testQuery2() {
        try (Session session = driver.session()) {

            try (Transaction tx = session.beginTransaction()) {
                tx.run("CREATE (a:Person {name: {name}, title: {title}})",
                        parameters("name", "Magnus", "title", "TrailBlaster"));
                tx.success();
            }

            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run(
                        "MATCH (a:Person) WHERE a.name = {name} "
                        + "RETURN a.name AS name, a.title AS title",
                        parameters("name", "Magnus"));
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
     * Close the DB driver
     */
    public void closeNeo4jDriver() {
        driver.close();
        System.out.println("Closed the driver.");
    }
}
