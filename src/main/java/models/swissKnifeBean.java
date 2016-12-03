/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.time.LocalDate;

/**
 *
 * @author SEPALMM
 */
public class swissKnifeBean {

    private LocalDate date;
    private String category;
    private String marketKey;
    private String marketName;
    private String marketGroup;
    private String cluster;
    private String finalCustomerKey;
    private String finalCustomerName;
    private String customerGroup;
    private String materialKey;
    private String materialName;
    private String mpg;
    private String assortmentGroup;
    private double netSales;
    private double netDirectMargin;
    private double invoiceQuantity;
    private String type;
    private double value;

    public swissKnifeBean() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMarketKey() {
        return marketKey;
    }

    public void setMarketKey(String marketKey) {
        this.marketKey = marketKey;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketGroup() {
        return marketGroup;
    }

    public void setMarketGroup(String marketGroup) {
        this.marketGroup = marketGroup;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getFinalCustomerKey() {
        return finalCustomerKey;
    }

    public void setFinalCustomerKey(String finalCustomerKey) {
        this.finalCustomerKey = finalCustomerKey;
    }

    public String getFinalCustomerName() {
        return finalCustomerName;
    }

    public void setFinalCustomerName(String finalCustomerName) {
        this.finalCustomerName = finalCustomerName;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public String getMaterialKey() {
        return materialKey;
    }

    public void setMaterialKey(String materialKey) {
        this.materialKey = materialKey;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMpg() {
        return mpg;
    }

    public void setMpg(String mpg) {
        this.mpg = mpg;
    }

    public String getAssortmentGroup() {
        return assortmentGroup;
    }

    public void setAssortmentGroup(String assortmentGroup) {
        this.assortmentGroup = assortmentGroup;
    }

    public double getNetSales() {
        return netSales;
    }

    public void setNetSales(double netSales) {
        this.netSales = netSales;
    }

    public double getNetDirectMargin() {
        return netDirectMargin;
    }

    public void setNetDirectMargin(double netDirectMargin) {
        this.netDirectMargin = netDirectMargin;
    }

    public double getInvoiceQuantity() {
        return invoiceQuantity;
    }

    public void setInvoiceQuantity(double invoiceQuantity) {
        this.invoiceQuantity = invoiceQuantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
