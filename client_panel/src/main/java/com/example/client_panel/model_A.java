package com.example.client_panel;

public class model_A {

    String book_name;
    String author_name;
    String cat_id;
    model_A(String book_name,String author_name,String cat_id)
    {
        this.book_name=book_name;
        this.author_name=author_name;
        this.cat_id=cat_id;
    }
    model_A()
    {

    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }
}
