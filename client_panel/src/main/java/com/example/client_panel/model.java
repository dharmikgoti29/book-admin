package com.example.client_panel;

public class model {
    String catagary;
    String cat_id;
    model()
    {

    }
    model(String catagary, String cat_id)
    {
        this.catagary = catagary;
        this.cat_id=cat_id;
    }

    public String getCatagary() {
        return catagary;
    }

    public void setCatagary(String catagary) {
        this.catagary = catagary;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }
}
