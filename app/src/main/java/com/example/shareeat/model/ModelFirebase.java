package com.example.shareeat.model;

import android.app.Activity;
import android.util.Log;
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
    public FirebaseAuth mAuth=FirebaseAuth.getInstance();

    public void signUpToFirebase(User user, String password, Activity activity){
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
                            Toast.makeText(activity, "Error User Created! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signInToFirebase(String email, String password, Activity activity, Model.SuccessListener listener){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "signInWithEmailSuccess");
                            Toast.makeText(activity, "Sign In Success!", Toast.LENGTH_SHORT).show();
                            listener.onComplete(true);
                        } else {
                            Log.d(TAG, "signInWithEmailFail");
                            Toast.makeText(activity, "Error Sign In! ", Toast.LENGTH_SHORT).show();
                            listener.onComplete(false);
                        }
                    }
                });
    }

//   public void signInWithEmailAndPassword {
//        @Override
//        public void onComplete(@NonNull Task<AuthResult> task) {
//            if (task.isSuccessful()){
//                Toast.makeText(Login.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            }  else {
//                Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        }
//    });

}
