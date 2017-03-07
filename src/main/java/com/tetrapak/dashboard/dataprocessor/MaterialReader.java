/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import com.tetrapak.dashboard.models.MaterialBean;
import java.io.IOException;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * This class reads material data from a csv file
 *
 * @author SEPALMM
 */
public class MaterialReader {

    private static final String CSV_FILENAME_MATERIALS = "materials_raw.csv";
    private static Map<String, MaterialBean> MATERIAL_MAP;

    /**
     * Sets up the processors used for the markets. There are 4 CSV columns, so
     * 4 processors are defined. Empty columns are read as null (hence the
     * NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessorsMaterials() {
        final CellProcessor[] processors = new CellProcessor[]{new UniqueHashCode(), // Material ID (must be unique)
            new NotNull(), // Material name
            new NotNull(), // Material pricing group
            new NotNull(), // Assortment group
            null, null, null // no processing required for ignored columns
    };
        return processors;
    }

    /**
     * Read Materials.
     */
    private static void readMaterials() throws Exception {
        ICsvBeanReader beanReader = null;
        MATERIAL_MAP = new HashMap<>();
        try {
            beanReader = new CsvBeanReader(new FileReader(
                    CSV_FILENAME_MATERIALS),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            beanReader.getHeader(true); // skip past the header (we're defining our own)
            /* Only map the first 4 columns - setting header elements to null
            means those columns are ignored. There are 7 columns in total*/
            final String[] header = {"materialKey", "materialName", "mpg", "assortmentGroup", null, null, null};
            final CellProcessor[] processors = MaterialReader.
                    getProcessorsMaterials();
            MaterialBean mtrl;
            while ((mtrl = beanReader.read(MaterialBean.class, header,
                    processors)) != null) {
                MATERIAL_MAP.put(mtrl.getMaterialKey(), mtrl);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (beanReader != null) {
                beanReader.close();
            }
        }
    }

    public static Map<String, MaterialBean> getMATERIAL_MAP() throws Exception {
        MaterialReader.readMaterials();
        return MATERIAL_MAP;
    }

}
