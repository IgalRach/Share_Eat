package com.example.shareeat.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.shareeat.MyApplication;
import com.example.shareeat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ModelFirebase {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    public static int recipesCounter=0;

    public void deleteRecipe(Recipe recipe) {
        db.collection("Deleted Recipes")
                .document(recipe.getId()).set(recipe.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "********* Recipe remove Successfully ************");
            }

        });
    }

    public interface GetAllRecipesListener{
        void onComplete(List<Recipe> list);
    }

    public void addRecipe(Recipe recipe, final Model.AddRecipeListener listener) {
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
                        User.getInstance().counter=recipeList.size();
                        Log.d("TAG","counterrrrrrrrrrr"+ User.getInstance().counter);
                        listener.onComplete(recipeList);

                    }
                });
    }

    public void getRecipe(String id, final Model.GetRecipeListener listener) {
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

    public void delete(Recipe recipe, Model.DeleteRecipeListener listener) {
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

    public static void registerUserAccount(final String fullName, String password, final String email,final String profilePic, final Listener<Boolean> listener) {
        if (firebaseAuth.getCurrentUser() != null){
            firebaseAuth.signOut();
        }
        if (firebaseAuth.getCurrentUser() == null &&
                fullName != null && !fullName.equals("") &&
                password != null && !password.equals("") &&
                email != null && !email.equals("")){
            Map<String,Object> data = new HashMap<>();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MyApplication.context, "User registered", Toast.LENGTH_SHORT).show();

                   // Map<String,Object> data = new HashMap<>();
                    data.put("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    data.put("profilePic",null);
                    data.put("fullName", fullName);
                    data.put("email", email);
                    data.put("password", password);
                    db.collection("userProfileData").document(email).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "User has created in userProfileData Collection");
                            User user = new User(null,fullName,email,profilePic);
                            setUserAppData(email);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MyApplication.context, "Fails to create user and upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                   // CreateUserProfile(email,fullName,password,profilePic);
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
    }

    public static void uploadUserData(final String fullName, final String email){
        //StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");

//        if (imageUri != null){
//            String imageName = username + "." + getExtension(imageUri);
//            final StorageReference imageRef = storageReference.child(imageName);

//            UploadTask uploadTask = imageRef.putFile(imageUri);
//            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()){
//                        throw task.getException();
//                    }
//                    return imageRef.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()){


                        //data.put("profileImageUrl", task.getResult().toString());
                        Map<String,Object> data = new HashMap<>();
                        data.put("fullName", fullName);
                        data.put("email", email);
                        //data.put("info", "NA");
                        db.collection("userProfileData").document(email).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MyApplication.context, "User has created in userProfileData Collection", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MyApplication.context, "Fails to create user and upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
//                    }
//                    else if (!task.isSuccessful()){
//                        Toast.makeText(MyApplication.context, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//        else {
//            Toast.makeText(FoodyApp.context, "Please choose a profile image", Toast.LENGTH_SHORT).show();
//        }
    }

    public static void updateUserProfile(User user){

        if(user.profilePic==null){
            Log.d("TAG","hereeeeeeeeeeee222");
            Task<QuerySnapshot> collection= db.collection("userProfileData").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(document.getData().get("id").equals(user.id)){
                            if(document.getData().get("profilePic")!=null){
                                String url= (String) document.getData().get("profilePic");
                                user.profilePic=url;
                            }
                            db.collection("userProfileData")
                                    .document(User.getInstance().email).set(user.toMap());
                        }
                    }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
            }else{
                db.collection("userProfileData")
                        .document(User.getInstance().email).set(user.toMap());
                Log.d("TAG","hereeeeeeeeeeee");
            }

    }

    public static void CreateUserProfile(String email,String fullName, String password, String profilePic) {
        Map<String, Object> data = new HashMap<>();
        if (email != null)
            data.put("email",email);
        if (fullName != null)
            data.put("fullName", fullName);
        if (profilePic != null)
            data.put("profilePic", profilePic);
        if (password != null)
            data.put("password", password);
        Log.d("TAG","email: "+email);
        Log.d("TAG","fullName: "+fullName);
        Log.d("TAG","profilePic: "+profilePic);
        Log.d("TAG","password: "+password);

        db.collection("userProfileData").document(User.getInstance().email).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MyApplication.context, "Profile Updates Successfully " , Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void loginUser(final String email, String password, final Listener<Boolean> listener){
        Log.d("TAG", "LOGINNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
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
        db.collection("userProfileData").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    User.getInstance().profilePic=(String) task.getResult().get("profilePic");
                    User.getInstance().fullName = (String) task.getResult().get("fullName");
                    User.getInstance().password = (String) task.getResult().get("password");
                    User.getInstance().email = email;
                    User.getInstance().id = firebaseAuth.getUid();
                }
            }
        });
    }

    public static void signOut(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }

//public static void getImageFromFireBase(Recipe recipe){
//
//    Task<QuerySnapshot> collection= db.collection("userProfileData").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//        @Override
//        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//            if (task.isSuccessful()) {
//
//                for (QueryDocumentSnapshot document : task.getResult()) {
//
//                    if(document.getData().get("id").equals(recipe.getUserId())){
//                        String name= (String) document.getData().get("fullName");
//                        viewHolder.nickname.setText(name);
//                        if(document.getData().get("profilePic")!=null){
//                            String url= (String) document.getData().get("profilePic");
//                            Log.d("TAG", document.getId() + " => " + document.getData().get("id"));
//                            Picasso.get().load(url).placeholder(R.drawable.ic_round_person_grey).into(viewHolder.profilePic);
//                        }
//                    }
//                }
//            } else {
//                Log.d("TAG", "Error getting documents: ", task.getException());
//            }
//        }
//    });
//}

    public void uploadImage(Bitmap imageBmp, String name,  Model.UploadImageListener listener){
        final StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }



}
