package com.example.shareeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shareeat.model.ModelFirebase;

public class register extends AppCompatActivity {

    EditText fullNameInput;
    EditText passwordInput;
    EditText emailInput;
    Button registerBtn;
    Button moveToLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.setTitle("Sign Up");

        fullNameInput = findViewById(R.id.register_fullName);
        passwordInput = findViewById(R.id.register_password);
        emailInput = findViewById(R.id.register_email);
        registerBtn = findViewById(R.id.register_register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelFirebase.registerUserAccount(fullNameInput.getText().toString(), passwordInput.getText().toString(),
                        emailInput.getText().toString(), new ModelFirebase.Listener<Boolean>() {
                            @Override
                            public void onComplete() {
                                startActivity(new Intent(register.this, MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onFail() {
                            }
                        });
            }
        });

        moveToLoginBtn = findViewById(R.id.register_login_btn);
        moveToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });
    }
}
