package com.example.shareeat.model;

public class User {
    private String id;
    private String nickName;
    private String email;

    public User(){

    }
    public User(String nickName, String email){
        this.nickName = nickName;
        this.email = email;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return this.nickName;
    }

    public void setFullName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
