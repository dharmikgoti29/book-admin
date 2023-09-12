package com.example.yourestore;

public class model_recycle {
    String book_title,book_author,description,image,subject;
    model_recycle()
    {

    }
    model_recycle(String book_title,String book_author,String description,String image,String subject)
    {
        this.book_title=book_title;
        this.book_author=book_author;
        this.description=description;
        this.image=image;
        this.subject=subject;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
