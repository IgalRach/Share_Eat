package com.example.shareeat.model;

import androidx.room.Entity;


@Entity
public class User {

    private static User MyUser = null;


    public String id;
    public String fullName;
    public String email;
    public String password;

    private User(){
        id = null;
        fullName = null;
        email = null;
        password = null;

    }

    public static User getInstance() {
        if (MyUser == null)
            MyUser = new User();

        return MyUser;
    }

}
