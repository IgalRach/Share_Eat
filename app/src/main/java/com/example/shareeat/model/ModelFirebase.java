package com.example.shareeat.model;


import android.app.Activity;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shareeat.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    interface GetAllRecipesListener{
        void onComplete(List<Recipe> list);
    }
    public void addRecipe(Recipe recipe, final Model.AddRecipeListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("recipes")
                .document(recipe.getId()).set(recipe.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "Recipe added Successfully");
                listener.onComplete();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error adding recipe", e);
                listener.onComplete();
            }
        });
    }

    public void getAllRecipes(Long lastUpdated, final GetAllRecipesListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastUpdated, 0);
        db.collection("recipes").whereGreaterThan("lastUpdated", ts)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Recipe> recipeList = new LinkedList<Recipe>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Recipe rcp = new Recipe();
                                rcp.fromMap(document.getData());
                                recipeList.add(rcp);
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                        listener.onComplete(recipeList);

                    }
                });
    }

    public void getRecipe(String id, final Model.GetRecipeListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("recipes").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Recipe recipe = null;
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc!=null) {
                        recipe = new Recipe();
                        recipe.fromMap(task.getResult().getData());
                    }
                }
                listener.onComplete(recipe);
            }
        });
    }

    public void updateStudent(Recipe recipe, Model.UpdateStudentListener listener) {
        addRecipe(recipe, listener);
    }

    public void delete(Recipe recipe, Model.DeleteRecipeListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("recipes").document(recipe.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete();
            }
        });
    }

    public interface Listener<T>{
        void onComplete();
        void onFail();
    }

    public static void registerUserAccount(final String fullName, String password, final String email, final Listener<Boolean> listener) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            firebaseAuth.signOut();
        }
        if (firebaseAuth.getCurrentUser() == null &&
                fullName != null && !fullName.equals("") &&
                password != null && !password.equals("") &&
                email != null && !email.equals("")){
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MyApplication.context, "User registered", Toast.LENGTH_SHORT).show();
                    listener.onComplete();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyApplication.context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onFail();
                }
            });
        }
        else {
            Toast.makeText(MyApplication.context, "Please fill all input fields and profile image", Toast.LENGTH_SHORT).show();
            listener.onFail();
        }
    }



    public static void loginUser(final String email, String password, final Listener<Boolean> listener){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (email != null && !email.equals("") && password != null && !password.equals("")){
            if (firebaseAuth.getCurrentUser() != null) {
                firebaseAuth.signOut();
            }
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MyApplication.context, "Login Succeeded!", Toast.LENGTH_SHORT).show();
                    setUserAppData(email);
                    listener.onComplete();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyApplication.context, "Failed to login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onFail();
                }
            });
        }
        else {
            Toast.makeText(MyApplication.context, "Please fill both data fields", Toast.LENGTH_SHORT).show();
        }

    }

    public static void setUserAppData(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();;
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        db.collection("userProfileData").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    User.getInstance().fullName = (String) task.getResult().get("fullName");
                    User.getInstance().password = (String) task.getResult().get("password");
                    User.getInstance().email = email;
                    User.getInstance().id = firebaseAuth.getUid();
                }
            }
        });
    }


}
