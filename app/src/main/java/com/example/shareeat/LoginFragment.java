package com.example.shareeat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
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

public class LoginFragment extends Fragment {

    EditText email, password;
    Button btnLogin, btnRegister;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.loginfrag_email);
        password = view.findViewById(R.id.loginfrag_password);
        btnRegister = view.findViewById(R.id.loginfragment_register_btn);
        btnLogin = view.findViewById(R.id.loginfragment_login_btn);
        progressBar = view.findViewById(R.id.loginfrag_progressBar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

//                if (TextUtils.isEmpty(userEmail)){
//                    email.setError("Email is required.");
//                    return;
//                }
//
//                if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
//                    email.setError("Please enter a valid email!");
//                }
//
//                if (TextUtils.isEmpty(userPassword)){
//                    password.setError("Password is required.");
//                    return;
//                }
//
//                if (userPassword.length()< 8){
//                    password.setError("Password must be >= 8 Characters");
//                    return;
//                }

                progressBar.setVisibility(View.VISIBLE);
                Model.instance.signInFB(userEmail, userPassword);
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_allPosts);

                /*
                Need To FIX
                 */
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        return view;
    }
}

//                if (email.equals("") && password.equals("")){
//                    Toast.makeText(getActivity(), "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Model.instance.signInFB(userEmail, userPassword, new Model.SuccessListener() {
//                        @Override
//                        public void onComplete(boolean result) {
//                            if (result) {
//                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_allPosts);
//                            } else {
//                                Toast.makeText(getActivity(), "Failed To Log In", Toast.LENGTH_SHORT).show();
//                            }
//                        }

//                }