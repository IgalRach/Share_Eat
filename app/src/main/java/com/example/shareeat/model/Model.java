package com.example.shareeat.model;

import android.app.Activity;

public class Model {

    public interface SuccessListener{
        void onComplete(boolean result);
    }

    public interface Listener<T>{
        void onComplete(T t);
    }

    ModelFirebase modelFirebase = new ModelFirebase();
    ModelSql modelSql = new ModelSql();

    private Activity mActivity;
    public final static Model instance = new Model();
    ModelFirebase fireBase = new ModelFirebase();

    public void signUpFB(User user, String password){
        fireBase.signUpToFirebase(user,password,mActivity);
    }

    public void signInFB(String email, String password, SuccessListener listener){
        fireBase.signInToFirebase(email,password,mActivity, listener);
    }
}
