package com.example.shareeat.model;

import android.widget.EditText;

import androidx.room.Entity;


@Entity
public class User {

    private static User MyUser = null;


    public String id;
    public String fullName;
    public String email;
    public String password;

    public User(){
        id = null;
        fullName = null;
        email = null;
        password = null;

    }

    public User(String fullNameInput, String passwordInput, String emailInput) {
        this.fullName = fullNameInput;
        this.password = passwordInput;
        this.email = emailInput;
    }

    public static User getInstance() {
        if (MyUser == null)
            MyUser = new User();

        return MyUser;
    }

}
