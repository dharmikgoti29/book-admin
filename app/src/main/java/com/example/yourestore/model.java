package com.example.yourestore;

public class model {
    String catagary;
    model()
    {

    }
    model(String catagary)
    {
        this.catagary = catagary;
    }

    public String getCatagary() {
        return catagary;
    }

    public void setCatagary(String catagary) {
        this.catagary = catagary;
    }
}
