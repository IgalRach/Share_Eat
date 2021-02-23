package com.example.shareeat.model;

public class User {
    private String id;
    private String fullName;
    private String email;

    public User(){

    }
    public User(String fullName, String email){
        this.fullName = fullName;
        this.email = email;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
