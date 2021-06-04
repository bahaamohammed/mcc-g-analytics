package com.bahaadev.mcc_g_analytics;

import java.io.Serializable;
import java.util.Map;

public class ProductsModle {

    private String CatName;
    private String Image;
    private String Name;
    private Map<String,Object> details;

    ProductsModle(){
    }

    public ProductsModle(String catName, String image, String name, Map<String, Object> details) {
        CatName = catName;
        Image = image;
        Name = name;
        this.details = details;
    }

    public String getCatName() {
        return CatName;
    }

    public String getImage() {
        return Image;
    }

    public String getName() {
        return Name;
    }

    public Serializable getDetails() {
        return details.toString();
    }
}
