/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import com.tetrapak.dashboard.database.Transactions;
import com.tetrapak.dashboard.models.MarketBean;
import java.util.Map;
import com.tetrapak.dashboard.models.MaterialBean;
import com.tetrapak.dashboard.models.TransactionBean;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;

/**
 * This application reads raw data from csv files, Transforms the raw data and
 * Loads processed and clean data into a Neo4j database. New data is added to
 * the database, never deleted.
 *
 * @author SEPALMM
 */
public class Main {

    /**
     * Makes a list of end-of-month dates, starting starting January two years
     * ago plus 36 months. It's rolling year-by-year adding a new set of 12
     * months as needed. It's not overwriting any data. The tree's root is named
     * `dashboard'.
     *
     * @return List of dates
     */
    private static List<LocalDate> makeThreeYearDateList() {

        LocalDate currentDate = LocalDate.now();
//        Find the year two years ago from current date
        int yearH2 = currentDate.getYear() - 2;
        LocalDate startingDate = LocalDate.of(yearH2, Month.JANUARY, 31);

        List<LocalDate> dateList = new LinkedList<>();

        for (int i = 0; i < (3 * 12); i++) {
            dateList.add(startingDate.plusMonths(i));
        }
        return dateList;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Transactions trx = new Transactions();
        try {
//            Load csv files (approximately 1 sec)
            Map<String, MarketBean> mkt = MarketReader.getMARKET_MAP();
            Map<String, MaterialBean> mtrl = MaterialReader.getMATERIAL_MAP();
            Map<Integer, TransactionBean> tr = TransactionReader.
                    getTRANSACTION_MAP();

//            Create service categories (approximately 1 sec)
            trx.createServiceCategories();

//            Make timeline tree (approximately 1 sec)
            trx.makeTimeLineTree(makeThreeYearDateList());

//            Load markets (approximately 3 sec)
            trx.loadMarketData(mkt);

//            Load materials (approximately 2.5 minutes)
            trx.loadMaterialData(mtrl);

//            Load customer data (approximately 1 sec)
            trx.loadCustomerData(tr);

//            Load transactions (approximately 15 sec)
            trx.loadTransactionData(tr);

        } catch (Exception e) {
        } finally {
            trx.closeNeo4jDriver();
            java.awt.Toolkit.getDefaultToolkit().beep();
        }

    }

}
