package com.example.yourestore;

public class model_user {
    String age,email,firstname,lastname,username;
    model_user(String age,String email,String firstname,String lastname,String username)
    {
        this.age=age;
        this.email=email;
        this.firstname=firstname;
        this.lastname=lastname;
        this.username=username;

    }
    model_user()
    {

    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
