/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 * This bean models materials, material pricing groups, and assortment groups
 *
 * @author SEPALMM
 */
public class MaterialBean {

    private String materialKey;
    private String materialName;
    private String mpg;
    private String assortmentGroup;

    public MaterialBean() {
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

}
