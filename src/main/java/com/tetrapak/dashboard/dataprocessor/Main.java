/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import models.MarketBean;
import java.util.Map;
import models.MaterialBean;

/**
 *this class process data
 * @author SEPALMM
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Map<String, MarketBean> mkt = MarketReader.getMARKET_MAP();
        Map<String, MaterialBean> mtrl = MaterialReader.getMATERIAL_MAP();

    }

}
