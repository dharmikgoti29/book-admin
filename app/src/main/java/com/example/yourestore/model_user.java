package com.example.yourestore;

public class model_user {
    String age,email,firstname,username,userid;
    model_user(String age,String email,String firstname,String username,String userid)
    {
        this.age=age;
        this.email=email;
        this.firstname=firstname;
        this.username=username;
        this.userid=userid;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
