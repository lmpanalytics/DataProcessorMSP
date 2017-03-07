/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import com.tetrapak.dashboard.database.Potentials;
import com.tetrapak.dashboard.database.Transactions;
import com.tetrapak.dashboard.models.InstalledBaseBean;
import com.tetrapak.dashboard.models.InvoiceBean;
import com.tetrapak.dashboard.models.MarketBean;
import java.util.Map;
import com.tetrapak.dashboard.models.MaterialBean;
import com.tetrapak.dashboard.models.ReferencePartBean;
import com.tetrapak.dashboard.models.TransactionBean;
import java.sql.Timestamp;

/**
 * This application reads raw data from csv files, Transforms the raw data and
 * Loads processed and cleansed data into a Neo4j graph database. New data is
 * added to the database, never deleted.
 *
 * The application's execution steps in the below Main Method are: 1) Create
 * Service Categories, 2) Load Market Data, 3) Load Customer Data, 4) Load
 * Transaction Data, and 5) Load Potentials Data.
 *
 * @author SEPALMM
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Transactions trx = new Transactions();
        Potentials pot = new Potentials();
        try {
            Timestamp timestampStart = new Timestamp(System.currentTimeMillis());
            System.out.println(
                    timestampStart + " :: Starting data load process...");

//            Load csv files (approximately 1 sec)
            Map<String, MarketBean> mkt = MarketMaker.getMarketMap();
            Map<String, MaterialBean> mtrl = MaterialReader.getMATERIAL_MAP();
            Map<String, ReferencePartBean> refMtrl = ReferencePartReader.
                    getREF_PART_MAP();
            Map<Integer, TransactionBean> tr = TransactionReader.
                    getTRANSACTION_MAP();
            Map<Integer, InstalledBaseBean> ib = InstalledBaseReader.getIB_MAP();
            Map<Integer, InvoiceBean> inv = InvoiceReader.getINVOICE_MAP();

//            Create service categories (approximately 1 sec)
            trx.createServiceCategories();

//            Load global market structure (approximately 3 sec)
            trx.loadMarketData(mkt);

//            Load customer data (approximately 1 sec)
            trx.loadCustomerData(tr, ib);

//            Load transactions (approximately 15 sec)
            trx.loadTransactionData(tr, inv, mtrl, refMtrl);

//            Load potentials (approximately xxx sec)
            pot.loadPotentialsData(ib);

            Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
            System.out.println(timestampEnd + " :: Finished data load process.");

        } catch (Exception e) {
            throw e;
        } finally {
            trx.closeNeo4jDriver();
            pot.closeNeo4jDriver();
            java.awt.Toolkit.getDefaultToolkit().beep();
        }

    }

}
