/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 * This bean models monthly transactions from the SwissKnife report
 *
 * @author SEPALMM
 */
public class TransactionBean {

//    private LocalDate date;
    private String monthYear;
    private String category;
    private String marketKey;
    private String finalCustomerKey;
    private String finalCustomerName;
    private String customerGroup;
    private String customerType;
    private String materialKey;
//    private double netSales;
    private String netSalesT;
//    private double directCost;
    private String directCostT;
//    private double invoiceQuantity;
    private String invoiceQuantityT;

    public TransactionBean() {
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
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

    public String getNetSalesT() {
        return netSalesT;
    }

    public void setNetSalesT(String netSalesT) {
        this.netSalesT = netSalesT;
    }

    public String getDirectCostT() {
        return directCostT;
    }

    public void setDirectCostT(String directCostT) {
        this.directCostT = directCostT;
    }

    public String getInvoiceQuantityT() {
        return invoiceQuantityT;
    }

    public void setInvoiceQuantityT(String invoiceQuantityT) {
        this.invoiceQuantityT = invoiceQuantityT;
    }

}
