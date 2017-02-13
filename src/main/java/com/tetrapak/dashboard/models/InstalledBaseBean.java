/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.models;

/**
 * This class models the Installed Base of Equipment from TecBase
 *
 * @author SEPALMM
 */
public class InstalledBaseBean {

    private String countryISOcode;
    private String finalCustomerKey;
    private String finalCustomerName;
    private String customerGroup;
    private String catalogueProfile;
    private double potSpareParts;
    private double potMaintenanceHrs;
    private double potMaintenance;

    public InstalledBaseBean() {
    }

    public InstalledBaseBean(String countryISOcode, String finalCustomerKey,
            String finalCustomerName, String customerGroup,
            String catalogueProfile, double potSpareParts,
            double potMaintenanceHrs, double potMaintenance) {
        this.countryISOcode = countryISOcode;
        this.finalCustomerKey = finalCustomerKey;
        this.finalCustomerName = finalCustomerName;
        this.customerGroup = customerGroup;
        this.catalogueProfile = catalogueProfile;
        this.potSpareParts = potSpareParts;
        this.potMaintenanceHrs = potMaintenanceHrs;
        this.potMaintenance = potMaintenance;
    }

    public String getCountryISOcode() {
        return countryISOcode;
    }

    public void setCountryISOcode(String countryISOcode) {
        this.countryISOcode = countryISOcode;
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

    public String getCatalogueProfile() {
        return catalogueProfile;
    }

    public void setCatalogueProfile(String catalogueProfile) {
        this.catalogueProfile = catalogueProfile;
    }

    public double getPotSpareParts() {
        return potSpareParts;
    }

    public void setPotSpareParts(double potSpareParts) {
        this.potSpareParts = potSpareParts;
    }

    public double getPotMaintenanceHrs() {
        return potMaintenanceHrs;
    }

    public void setPotMaintenanceHrs(double potMaintenanceHrs) {
        this.potMaintenanceHrs = potMaintenanceHrs;
    }

    public double getPotMaintenance() {
        return potMaintenance;
    }

    public void setPotMaintenance(double potMaintenance) {
        this.potMaintenance = potMaintenance;
    }

}
