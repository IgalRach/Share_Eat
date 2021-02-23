package com.example.shareeat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.User;

public class RegisterFragment extends Fragment {

    EditText fullName, email, password;
    Button btnLogin, btnRegister;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        fullName = view.findViewById(R.id.registerfrag_fullName);
        password = view.findViewById(R.id.registerfrag_password);
        email = view.findViewById(R.id.registerfrag_email);
        progressBar = view.findViewById(R.id.registerfrag_progressBar);
        btnLogin = view.findViewById(R.id.registerfragment_login_btn);
        btnRegister = view.findViewById(R.id.registerfragment_register_btn);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userFullName = fullName.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(userEmail)){
                    email.setError("Email is required.");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    email.setError("Please enter a valid email!");
                }

                if (TextUtils.isEmpty(userPassword)){
                    password.setError("Password is required.");
                    return;
                }

                if (userPassword.length()< 8){
                    password.setError("Password must be >= 8 Characters");
                    return;
                }

                progressBar.setVisibility(view.VISIBLE);

                final User user = new User(userFullName, userEmail);
                Model.instance.signUpFB(user, userPassword);

                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_allPosts);
//                if (userEmail.equals("") || userPassword.equals("")){
//                    Toast.makeText(getActivity(), "Please enter email&password", Toast.LENGTH_SHORT).show();
//                } else {
//                    final User user = new User(userFullName, userEmail);
//                    Model.instance.signUpFB(user, userPassword);
//                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_allPosts);
//                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });


        return view;
    }
}