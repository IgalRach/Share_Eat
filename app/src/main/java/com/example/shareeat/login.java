package com.example.shareeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.shareeat.model.ModelFirebase;


public class login extends AppCompatActivity {

    EditText email, password;
    Button btnLogin, btnRegister;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.loginfrag_email);
        password = findViewById(R.id.loginfrag_password);

        btnRegister = findViewById(R.id.login_register_btn);
        btnLogin = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.loginfrag_progressBar);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(userEmail)){
                    email.setError("Email is required.");
                    email.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    email.setError("Please provide valid email!");
                    email.requestFocus();
                    return;
                }

                if(userPassword.isEmpty()){
                    password.setError("Password is required!");
                    password.requestFocus();
                    return;
                }
                if(userPassword.length()<8 || userPassword.length()>20){
                    password.setError("Password length must be 8-20 characters!");
                    password.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                ModelFirebase.signInToFirebase(userEmail,userPassword,login.this);

                startActivity(new Intent(login.this,MainActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,register.class));
            }
        });
    }
}