/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.models;

/**
 * This bean models local Assortment and MPG assignments in local markets as
 * captured in the Invoice report.
 *
 * @author SEPALMM
 */
public class InvoiceBean {

    private String marketKey;
    private String materialKey;
    private String assortmentGroup;
    private String mpg;

    public InvoiceBean() {
    }

    public String getMarketKey() {
        return marketKey;
    }

    public void setMarketKey(String marketKey) {
        this.marketKey = marketKey;
    }

    public String getMaterialKey() {
        return materialKey;
    }

    public void setMaterialKey(String materialKey) {
        this.materialKey = materialKey;
    }

    public String getAssortmentGroup() {
        return assortmentGroup;
    }

    public void setAssortmentGroup(String assortmentGroup) {
        this.assortmentGroup = assortmentGroup;
    }

    public String getMpg() {
        return mpg;
    }

    public void setMpg(String mpg) {
        this.mpg = mpg;
    }

}
