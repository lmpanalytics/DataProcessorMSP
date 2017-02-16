/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.models;

/**
 * This class models reference parts
 *
 * @author SEPALMM
 */
public class ReferencePartBean {

    private String refMaterialKey;
    private String refMaterialName;

    public ReferencePartBean() {
    }

    public String getRefMaterialKey() {
        return refMaterialKey;
    }

    public void setRefMaterialKey(String refMaterialKey) {
        this.refMaterialKey = refMaterialKey;
    }

    public String getRefMaterialName() {
        return refMaterialName;
    }

    public void setRefMaterialName(String refMaterialName) {
        this.refMaterialName = refMaterialName;
    }

}
