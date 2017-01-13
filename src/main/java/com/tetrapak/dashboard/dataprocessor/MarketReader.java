/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import com.tetrapak.dashboard.models.MarketBean;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * This class reads market data from a csv file
 *
 * @author SEPALMM
 */
public class MarketReader {

    private static String CSV_FILENAME_MARKETS = "markets_raw.csv";
    private static Map<String, MarketBean> MARKET_MAP;

    /**
     * Sets up the processors used for the markets. There are 4 CSV columns, so
     * 4 processors are defined. Empty columns are read as null (hence the
     * NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessorsMarkets() {
        final CellProcessor[] processors = new CellProcessor[]{new UniqueHashCode(), // Market ID (must be unique)
            new NotNull(), // Market name
            new NotNull(), // Markrt Group
            new NotNull(), // Cluster
            null, null, null // no processing required for ignored columns
    };
        return processors;
    }

    /**
     * Read Market.
     */
    private static void readMarkets() throws Exception {
        ICsvBeanReader beanReader = null;
        MARKET_MAP = new HashMap<>();
        try {
            beanReader = new CsvBeanReader(new FileReader(
                    CSV_FILENAME_MARKETS),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            beanReader.getHeader(true); // skip past the header (we're defining our own)
            /* Only map the first 4 columns - setting header elements to null
            means those columns are ignored. There are 7 columns in total*/
            final String[] header = {"marketKey", "marketName", "marketGroup", "cluster", null, null, null};
            final CellProcessor[] processors = MarketReader.
                    getProcessorsMarkets();
            MarketBean mkt;
            while ((mkt = beanReader.read(MarketBean.class, header,
                    processors)) != null) {
//                System.out.println(String.format("lineNo=%s, rowNo=%s, market=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), mkt.getMarketName()));
                MARKET_MAP.put(mkt.getMarketKey(), mkt);
            }
        } finally {
            if (beanReader != null) {
                beanReader.close();
            }
        }
    }

    public static Map<String, MarketBean> getMARKET_MAP() throws Exception {
        MarketReader.readMarkets();
        return MARKET_MAP;
    }

}
