/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.models;

/**
 * This bean models geographical data of Countries, Markets, Market Groups and
 * Clusters.
 *
 * @author SEPALMM
 */
public class MarketBean {

    private String countryISOkey;
    private String countryISOname;
    private String marketKey;
    private String marketName;
    private String marketGroupKey;
    private String marketGroupName;
    private String cluster;

    /**
     * 
     * @param countryISOkey from IB report BO
     * @param countryISOname http://www.iso.org/iso/home/standards/country_codes/country_names_and_code_elements_txt-temp.htm
     * @param marketKey from Special Ledger report BO
     * @param marketName from Special Ledger report BO
     * @param marketGroupKey from Special Ledger report BO
     * @param marketGroupName from Special Ledger report BO
     * @param cluster from Special Ledger report BO
     */
    public MarketBean(String countryISOkey, String countryISOname,
            String marketKey, String marketName, String marketGroupKey,
            String marketGroupName, String cluster) {
        this.countryISOkey = countryISOkey;
        this.countryISOname = countryISOname;
        this.marketKey = marketKey;
        this.marketName = marketName;
        this.marketGroupKey = marketGroupKey;
        this.marketGroupName = marketGroupName;
        this.cluster = cluster;
    }

    public String getCountryISOkey() {
        return countryISOkey;
    }

    public void setCountryISOkey(String countryISOkey) {
        this.countryISOkey = countryISOkey;
    }

    public String getCountryISOname() {
        return countryISOname;
    }

    public void setCountryISOname(String countryISOname) {
        this.countryISOname = countryISOname;
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

    public String getMarketGroupKey() {
        return marketGroupKey;
    }

    public void setMarketGroupKey(String marketGroupKey) {
        this.marketGroupKey = marketGroupKey;
    }

    public String getMarketGroupName() {
        return marketGroupName;
    }

    public void setMarketGroupName(String marketGroupName) {
        this.marketGroupName = marketGroupName;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

}
