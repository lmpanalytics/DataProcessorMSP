/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import com.tetrapak.dashboard.models.ReferencePartBean;
import java.io.IOException;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * This class reads Reference part data from a csv file
 *
 * @author SEPALMM
 */
public class ReferencePartReader {

    private static final String CSV_FILENAME_REF_PARTS = "reference_parts.csv";
    private static Map<String, ReferencePartBean> REF_PART_MAP;

    /**
     * Sets up the processors used for the reference parts. There are 2 CSV
     * columns, so 2 processors are defined. Empty columns are read as null
     * (hence the NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessorsMaterials() {
        final CellProcessor[] processors = new CellProcessor[]{new UniqueHashCode(), // Material ID (must be unique)
            new NotNull(), // Material name
    };
        return processors;
    }

    /**
     * Read Reference parts.
     */
    private static void readRefPartMaterials() throws Exception {
        ICsvBeanReader beanReader = null;
        REF_PART_MAP = new HashMap<>();
        try {
            beanReader = new CsvBeanReader(new FileReader(
                    CSV_FILENAME_REF_PARTS),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            beanReader.getHeader(true); // skip past the header (we're defining our own)
            /* Only map the first 2 columns - setting header elements to null
            means those columns are ignored. There are 2 columns in total*/
            final String[] header = {"refMaterialKey", "refMaterialName"};
            final CellProcessor[] processors = ReferencePartReader.
                    getProcessorsMaterials();
            ReferencePartBean refMtrl;
            while ((refMtrl = beanReader.read(ReferencePartBean.class, header,
                    processors)) != null) {
                REF_PART_MAP.put(refMtrl.getRefMaterialKey(), refMtrl);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (beanReader != null) {
                beanReader.close();
            }
        }
    }

    public static Map<String, ReferencePartBean> getREF_PART_MAP() throws Exception {
        ReferencePartReader.readRefPartMaterials();
        return REF_PART_MAP;
    }
}
