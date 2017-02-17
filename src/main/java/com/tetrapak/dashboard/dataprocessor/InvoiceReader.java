/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import com.tetrapak.dashboard.models.InvoiceBean;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * This class reads invoice data from a csv file.
 *
 * @author SEPALMM
 */
public class InvoiceReader {

    private static final String CSV_FILENAME_INVOICES = "invoices_raw.csv";
    private static Map<Integer, InvoiceBean> INVOICE_MAP;

    /**
     * Sets up the processors used for the invoices. There are 4 CSV columns, so
     * 4 processors are defined. Empty columns are read as null (hence the
     * NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessorsInvoices() {
        final CellProcessor[] processors = new CellProcessor[]{
            new NotNull(), // Market ID
            new NotNull(), // MaterialNumber
            new NotNull(), // Assortment group
            new NotNull(), // Material pricing group           
            null // no processing required for ignored columns
        };
        return processors;
    }

    /**
     * Read Materials.
     */
    private static void readInvoices() throws Exception {
        ICsvBeanReader beanReader = null;
        INVOICE_MAP = new HashMap<>();
        try {
            beanReader = new CsvBeanReader(new FileReader(
                    CSV_FILENAME_INVOICES),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            beanReader.getHeader(true); // skip past the header (we're defining our own)
            /* Only map the first 4 columns - setting header elements to null
            means those columns are ignored. There are 5 columns in total*/
            final String[] header = {"marketKey", "materialKey", "assortmentGroup", "mpg", null};
            final CellProcessor[] processors = InvoiceReader.
                    getProcessorsInvoices();
            InvoiceBean invoice = new InvoiceBean();

            while ((invoice = beanReader.read(InvoiceBean.class, header,
                    processors)) != null) {
//                Filter out not assigned mtrl numbers
                if (!invoice.getMaterialKey().equals("#")) {
//                Create composite key
                    Integer compositeKey = (invoice.getMarketKey() + invoice.
                            getMaterialKey()).hashCode();
                    INVOICE_MAP.put(compositeKey, invoice);
                }
            }
        } finally {
            if (beanReader != null) {
                beanReader.close();
            }
        }
    }

    public static Map<Integer, InvoiceBean> getINVOICE_MAP() throws Exception {
        InvoiceReader.readInvoices();
        return INVOICE_MAP;
    }
}
