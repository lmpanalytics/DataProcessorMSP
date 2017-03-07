/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import com.tetrapak.dashboard.database.Utilities;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import com.tetrapak.dashboard.models.TransactionBean;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
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

    private static Map<Integer, TransactionBean> TRANSACTION_MAP;

    /**
     * Sets up the processors used for the transactions. There are 14 CSV
     * columns, so 14 processors are defined. Empty columns are read as null
     * (hence the NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessorsTransactions() {

        final CellProcessor[] processors = new CellProcessor[]{
            new NotNull(), // Month/Year
            new NotNull(new ParseInt()), // Number of days
            new NotNull(new ParseInt()), // Month
            new NotNull(new ParseInt()), // Year
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
     * Reads Transaction files. Reads and processes all files found in the
     * transactions directory. This directory shall be located in the home
     * directory and be named 'transactions'. Drop any transaction file to be
     * processed in that directory .
     */
    private static void readTransactions() throws Exception {
        Integer keyCounter = 0;
        ICsvBeanReader beanReader = null;
        TRANSACTION_MAP = new HashMap<>();

        try {
//            Initiate list of transaction files
            List<String> fileList = new ArrayList<>();
//            Make path to directory with transaction files
            Path dir = Paths.get("transactions\\");
//            Get file contents of the transactions directory
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path file : stream) {
                    Path path = Paths.get("transactions\\" + file.getFileName());
//                    Add the file paths to the list
                    fileList.add(path.toString());
                }
            } catch (IOException | DirectoryIteratorException x) {
                // IOException can never be thrown by the iteration.
                // In this snippet, it can only be thrown by newDirectoryStream.
                System.err.println(x);
            }

//            Read and process all files found in the transactions directory
            for (String f : fileList) {
                System.out.printf("Processing %s%n", f);
                beanReader = new CsvBeanReader(new FileReader(f),
                        CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
                beanReader.getHeader(true);
                /* skip past the header 
            (we're defining our own) There are 14 columns in total*/
                final String[] header = {"monthYear", "numberOfDays", "month",
                    "year", "category", "marketKey", "finalCustomerKey",
                    "finalCustomerName", "customerGroup", "customerType",
                    "materialKey", "netSalesT", "directCostT", "invoiceQuantityT"};
                final CellProcessor[] processors = TransactionReader.
                        getProcessorsTransactions();
                TransactionBean tr;
                while ((tr = beanReader.read(TransactionBean.class, header,
                        processors)) != null) {

//                Initialize variables
                    double myNetSales = 0d;
                    double myDirectCost = 0d;
                    double qty = 0d;
                    String myType = "";
                    String myCustomerGroup = "";

//                Make compiled key
                    keyCounter++;
                    String key = keyCounter.toString() + tr.getMonthYear();
//                Make a local date
                    LocalDate myDate = LocalDate.of(tr.getYear(), tr.getMonth(),
                            tr.
                                    getNumberOfDays());
//                    Assign '0' to empty customer group names
                    if (tr.getCustomerGroup().equals("") || tr.
                            getCustomerGroup().
                            matches("^\\s+$") || tr.getCustomerGroup().isEmpty()) {
                        System.out.println("jag är här");
                        myCustomerGroup = "0";
                    } else {
                        myCustomerGroup = tr.getCustomerGroup();
                    }
//                Convert sales and cost to Double, and handle null exceptions
                    if (tr.getNetSalesT() != null) {
                        myNetSales = Double.parseDouble(tr.getNetSalesT().
                                replaceAll("\\s\\D+$", "").replaceAll("\\s", ""));
                    }
                    if (tr.getDirectCostT() != null) {
                        myDirectCost = Double.parseDouble(tr.getDirectCostT().
                                replaceAll("\\s\\D+$", "").replaceAll("\\s", ""));
                    }

                    /* Extract qty to Double and Type */
                    if (tr.getInvoiceQuantityT() != null) {
//                    Remove all Letters, white space
                        qty = Utilities.
                                extractQuantity(tr.getInvoiceQuantityT());
//                    Remove all white space, minus sign, and digits
                        myType = Utilities.extractQuantityType(tr.
                                getInvoiceQuantityT());
//                    If Type is KPC multiply qty with 1e3
                        if (myType.equals("KPC")) {
                            qty = 1e3 * qty;
                        }
                    }
//                  Put transactions to transaction map
                    TRANSACTION_MAP.put(key.hashCode(), new TransactionBean(
                            myDate, tr.getCategory(), tr.getMarketKey(), tr.
                            getFinalCustomerKey(), tr.getFinalCustomerName(),
                            myCustomerGroup, tr.getCustomerType(),
                            tr.getMaterialKey(), myNetSales, myDirectCost,
                            qty, myType, true));
                }
            }
        } catch (IOException e) {
            throw e;
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
