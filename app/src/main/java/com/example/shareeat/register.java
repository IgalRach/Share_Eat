package com.example.shareeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.shareeat.model.ModelFirebase;
import com.example.shareeat.model.User;


public class register extends AppCompatActivity {

    EditText nickname, email, password;
    Button btnLogin, btnRegister;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nickname = findViewById(R.id.register_nickname);
        password = findViewById(R.id.register_password);
        email = findViewById(R.id.register_email);

        btnLogin = findViewById(R.id.register_login_btn);
        btnRegister = findViewById(R.id.register_register_btn);

        progressBar = findViewById(R.id.registerfrag_progressBar);

        btnRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String userNickname = nickname.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();


                if(userNickname.isEmpty()){
                    nickname.setError("Nickname is required!");
                    nickname.requestFocus();
                    return;
                }

                if(userEmail.isEmpty()){
                    email.setError("Email is required!");
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
                final User user = new User(userNickname, userEmail);
                ModelFirebase.signUpToFirebase(user,userPassword,register.this);
                startActivity(new Intent(register.this,MainActivity.class));

            }
        });

    }



}