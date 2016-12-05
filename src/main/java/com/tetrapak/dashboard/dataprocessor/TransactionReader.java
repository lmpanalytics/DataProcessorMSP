/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import models.TransactionBean;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * This class reads transaction data from a csv file
 *
 * @author SEPALMM
 */
public class TransactionReader {

    private static String CSV_FILENAME_TRANSACTIONS = "transactions_raw.csv";
    private static Map<Integer, TransactionBean> TRANSACTION_MAP;

    /**
     * Sets up the processors used for the transactions. There are 11 CSV
     * columns, so 11 processors are defined. Empty columns are read as null
     * (hence the NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessorsTransactions() {
        final CellProcessor[] processors = new CellProcessor[]{
            new NotNull(), // Month/Date
            new NotNull(), // Service category
            new NotNull(), // Market Key
            new NotNull(), // Final Customer Key
            new NotNull(), // Final Customer Name
            new NotNull(), // Customer Group
            new NotNull(), // Customer Type
            new NotNull(), // Material number
            new Optional(), // Net Sales (Type truncated)
            new Optional(), // Direct Cost (Type truncated)
            new Optional() // Invoice Quantity (Type truncated)
        };
        return processors;
    }

    /**
     * Read Market.
     */
    private static void readTransactions() throws Exception {
        Integer KeyCounter = 0;
        ICsvBeanReader beanReader = null;
        TRANSACTION_MAP = new HashMap<>();
        try {
            beanReader = new CsvBeanReader(new FileReader(
                    CSV_FILENAME_TRANSACTIONS),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            beanReader.getHeader(true);
            /* skip past the header 
            (we're defining our own) There are 11 columns in total*/
            final String[] header = {"monthYear", "category", "marketKey", "finalCustomerKey", "finalCustomerName", "customerGroup", "customerType", "materialKey", "netSalesT", "directCostT", "invoiceQuantityT"};
            final CellProcessor[] processors = TransactionReader.
                    getProcessorsTransactions();
            TransactionBean tr;
            while ((tr = beanReader.read(TransactionBean.class, header,
                    processors)) != null) {
                KeyCounter++;
                String key = KeyCounter.toString() + tr.getMonthYear();
                TRANSACTION_MAP.put(key.hashCode(), tr);
            }
        } finally {
            if (beanReader != null) {
                beanReader.close();
            }
        }
    }

    public static Map<Integer, TransactionBean> getTRANSACTION_MAP() throws Exception {
        TransactionReader.readTransactions();
        return TRANSACTION_MAP;
    }

}
