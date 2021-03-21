package com.example.shareeat.model;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class ModelFirebase {

    public FirebaseDatabase database = FirebaseDatabase.getInstance();



    public static void signUpToFirebase(User user, String password, Activity activity){
         FirebaseAuth mAuth = FirebaseAuth.getInstance();
         mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(activity, "User Created Success!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, "Error User Created! ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(activity, "User creation failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void signInToFirebase(String email, String password, Activity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(activity, "LOGIN Success!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // NEED TO FIX THE NAVIGATE WHEN YOU'RE NOT LOGIN SUCCESS
                                        Toast.makeText(activity, "LOGIN FAILED! ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(activity, "LOGIN FAILED! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
