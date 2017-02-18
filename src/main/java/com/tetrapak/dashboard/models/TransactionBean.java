/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.models;

import java.time.LocalDate;

/**
 * This bean models monthly transactions from the SwissKnife report
 *
 * @author SEPALMM
 */
public class TransactionBean {

    private String monthYear;
    private int numberOfDays;
    private int month;
    private int year;
    private LocalDate date;
    private String category;
    private String marketKey;
    private String finalCustomerKey;
    private String finalCustomerName;
    private String customerGroup;
    private String customerType;
    private String materialKey;
    private double netSales;
    private String netSalesT;
    private double directCost;
    private String directCostT;
    private double invoiceQuantity;
    private String invoiceQuantityT;
    private String typeOfQty;
    private Boolean sourceBO;

    public TransactionBean() {
    }

    public TransactionBean(LocalDate date, String category, String marketKey,
            String finalCustomerKey, String finalCustomerName,
            String customerGroup, String customerType, String materialKey,
            double netSales, double directCost, double invoiceQuantity,
            String typeOfQty, Boolean sourceBO) {
        this.date = date;
        this.category = category;
        this.marketKey = marketKey;
        this.finalCustomerKey = finalCustomerKey;
        this.finalCustomerName = finalCustomerName;
        this.customerGroup = customerGroup;
        this.customerType = customerType;
        this.materialKey = materialKey;
        this.netSales = netSales;
        this.directCost = directCost;
        this.invoiceQuantity = invoiceQuantity;
        this.typeOfQty = typeOfQty;
        this.sourceBO = sourceBO;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getMaterialKey() {
        return materialKey;
    }

    public void setMaterialKey(String materialKey) {
        this.materialKey = materialKey;
    }

    public double getNetSales() {
        return netSales;
    }

    public void setNetSales(double netSales) {
        this.netSales = netSales;
    }

    public String getNetSalesT() {
        return netSalesT;
    }

    public void setNetSalesT(String netSalesT) {
        this.netSalesT = netSalesT;
    }

    public double getDirectCost() {
        return directCost;
    }

    public void setDirectCost(double directCost) {
        this.directCost = directCost;
    }

    public String getDirectCostT() {
        return directCostT;
    }

    public void setDirectCostT(String directCostT) {
        this.directCostT = directCostT;
    }

    public double getInvoiceQuantity() {
        return invoiceQuantity;
    }

    public void setInvoiceQuantity(double invoiceQuantity) {
        this.invoiceQuantity = invoiceQuantity;
    }

    public String getInvoiceQuantityT() {
        return invoiceQuantityT;
    }

    public void setInvoiceQuantityT(String invoiceQuantityT) {
        this.invoiceQuantityT = invoiceQuantityT;
    }

    public String getTypeOfQty() {
        return typeOfQty;
    }

    public void setTypeOfQty(String typeOfQty) {
        this.typeOfQty = typeOfQty;
    }

    public Boolean isSourceBO() {
        return sourceBO;
    }

    public void setSourceBO(Boolean sourceBO) {
        this.sourceBO = sourceBO;
    }

}
