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
 * This class processes data
 *
 * @author SEPALMM
 */
public class Main {

    /**
     * Makes a list of end-of-month dates, starting in January two years ago,
     * and three years in to the future.
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
            Map<String, MarketBean> mkt = MarketReader.getMARKET_MAP();
            Map<String, MaterialBean> mtrl = MaterialReader.getMATERIAL_MAP();
            Map<Integer, TransactionBean> tr = TransactionReader.
                    getTRANSACTION_MAP();

            trx.makeTimeLineTree(makeThreeYearDateList());
            
            trx.loadMarketData(mkt);
            
        } catch (Exception e) {
        } finally {
            trx.closeNeo4jDriver();
            java.awt.Toolkit.getDefaultToolkit().beep();
        }

    }

}
