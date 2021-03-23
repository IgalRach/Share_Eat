package com.example.shareeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.shareeat.model.ModelFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;
    Button loginBtn;
    Button moveToRegisterBtn;
    FirebaseAuth firebaseAuth;

    //ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            ModelFirebase.setUserAppData(firebaseAuth.getCurrentUser().getEmail());
            startActivity(new Intent(login.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);

        this.setTitle("Login");

        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);

        loginBtn = findViewById(R.id.login_login_btn);
        moveToRegisterBtn = findViewById(R.id.movetoRegister);

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ModelFirebase.loginUser(emailInput.getText().toString(), passwordInput.getText().toString(), new ModelFirebase.Listener<Boolean>() {
                    @Override
                    public void onComplete() {
                        startActivity(new Intent(login.this, MainActivity.class));

                        finish();
                    }
                    @Override
                    public void onFail() {
                    }

                });
                //progressBar.setVisibility(View.VISIBLE);
            }
        });


        moveToRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,register.class));
            }
        });

    }

    private void toRegisterPage() {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);

    }
}