/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import com.tetrapak.dashboard.database.Utilities;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import com.tetrapak.dashboard.models.InstalledBaseBean;
import java.io.IOException;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * This class reads Installed Base data from a csv file
 *
 * @author SEPALMM
 */
public class InstalledBaseReader {

    private static final String CSV_FILENAME_IB = "potentials_raw.csv";
    private static Map<Integer, InstalledBaseBean> IB_MAP;

    /**
     * Sets up the processors used for the transactions. There are 8 CSV
     * columns, so 8 processors are defined. Empty columns are read as null
     * (hence the NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessorsIB() {

        final CellProcessor[] processors = new CellProcessor[]{
            new NotNull(), // Country ISO Code
            new NotNull(), // Final Customer Key
            new NotNull(), // Final Customer Name
            new Optional(), // Customer Group
            new Optional(), // Catalogue Profile
            new NotNull(new ParseDouble()), // Potential SpareParts
            new NotNull(new ParseDouble()), // Potential Maintenance Hrs
            new NotNull(new ParseDouble()) // Potential Maintenance
        };
        return processors;
    }

    /**
     * Read Installed Base.
     */
    private static void readInstalledBase() throws Exception {

        Integer keyCounter = 0;
        ICsvBeanReader beanReader = null;
        IB_MAP = new HashMap<>();

        try {
            beanReader = new CsvBeanReader(new FileReader(
                    CSV_FILENAME_IB),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            beanReader.getHeader(true);
            /* skip past the header 
            (we're defining our own) There are 8 columns in total*/
            final String[] header = {"countryISOcode", "finalCustomerKey",
                "finalCustomerName", "customerGroup", "assortmentConsumer",
                "potSpareParts", "potMaintenanceHrs", "potMaintenance"};
            final CellProcessor[] processors = InstalledBaseReader.
                    getProcessorsIB();
            InstalledBaseBean ib;
            while ((ib = beanReader.read(InstalledBaseBean.class, header,
                    processors)) != null) {

//                Initialize variables
                String myCustomerGroup = "0";
                String myAssortmentConsumer = "";

//                Handle null pointers
                if (ib.getCustomerGroup() != null) {
                    myCustomerGroup = ib.getCustomerGroup();
//                    Assign '0' to empty customer group names
                    if (myCustomerGroup.equals("") || myCustomerGroup.matches(
                            "^\\s+$") || myCustomerGroup.isEmpty()) {
                        myCustomerGroup = "0";
                    }
                }
                if (ib.getAssortmentConsumer() != null) {
                    myAssortmentConsumer = ib.getAssortmentConsumer();
                }

                String myFinalCustomerKey = Utilities.createTPformatCustNo(
                        ib.getFinalCustomerKey());

                String myCustomerName = Utilities.cleanCustomerName(ib.
                        getFinalCustomerName());

                String assortment = Utilities.assignAssortmentGroup(
                        myAssortmentConsumer);

// Add IB to IB Map, on condition that the customer number is not "#".
                if (!myFinalCustomerKey.equals("#")) {
                    IB_MAP.put(keyCounter, new InstalledBaseBean(
                            ib.getCountryISOcode(), myFinalCustomerKey,
                            myCustomerName, myCustomerGroup,
                            assortment, ib.getPotSpareParts(),
                            ib.getPotMaintenanceHrs(), ib.getPotMaintenance()));
                }
                keyCounter++;
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (beanReader != null) {
                beanReader.close();
            }
        }
    }

    public static Map<Integer, InstalledBaseBean> getIB_MAP() throws Exception {
        readInstalledBase();
        return IB_MAP;
    }

}
